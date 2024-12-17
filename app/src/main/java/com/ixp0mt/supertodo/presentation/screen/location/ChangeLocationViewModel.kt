package com.ixp0mt.supertodo.presentation.screen.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.model.element.IElementMeta
import com.ixp0mt.supertodo.domain.usecase.element.FilterValidParentElementsUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetLocationAsElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetSubElementsUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetTypeElementUseCase
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.navigation.Routes
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.BaseViewModel
import com.ixp0mt.supertodo.presentation.util.ElementCardSimpleInfo
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeLocationViewModel @Inject constructor(
    private val getLocationAsElementUseCase: GetLocationAsElementUseCase,
    private val getSubElementsUseCase: GetSubElementsUseCase
) : BaseViewModel() {
    private val _locationAsElement = MutableLiveData<IElementMeta?>(null)
    val locationAsElement: LiveData<IElementMeta?> = _locationAsElement

    private val _listSubElements = MutableLiveData<List<ElementCardSimpleInfo>>(emptyList())
    val listSubElements: LiveData<List<ElementCardSimpleInfo>> = _listSubElements

    private val _iconIdLocation = MutableLiveData<Int>(null)
    val iconIdLocation: LiveData<Int> = _iconIdLocation

    private val _infoElementLocation = MutableLiveData<ElementParam?>(null)

    private val _infoClickSave = MutableLiveData<ElementParam?>(null)
    val infoClickSave: LiveData<ElementParam?> = _infoClickSave

    private val _infoClickSubElement = MutableLiveData<ElementParam?>(null)
    val infoClickSubElement: LiveData<ElementParam?> = _infoClickSubElement

    private val _showBackLocation = MutableLiveData<Boolean>(false)
    val showBackLocation: LiveData<Boolean> = _showBackLocation


    override fun clearData() {
        _infoClickSave.value = null
        _infoClickSubElement.value = null
    }

    override suspend fun initData(screenState: ScreenState) {
        val idElementLocation = screenState.currentArgs[Routes.ChangeLocation.ID]?.toLongOrNull() ?: return
        val typeElementLocation = screenState.currentArgs[Routes.ChangeLocation.TYPE]?.let { TypeElement.convert(it) } ?: return

        val param = ElementParam(typeElementLocation, idElementLocation)
        val locationAsElement = getLocationAsElementUseCase(param)

        // Если ID элемента нет, значит элемент создаётся в данный момент
        val idChangedElement: Long = screenState.previousArgs["ID"]?.toLongOrNull() ?: 0
        val typeChangedElement = screenState.previousRawRoute?.let { TypeElement.convert(it) } ?: return


        val listPotentialParents = getSubElementsUseCase(typeElementLocation, idElementLocation)
        val filterValidParentElementsUseCase = FilterValidParentElementsUseCase()
        val listValidParents = filterValidParentElementsUseCase(typeChangedElement, listPotentialParents)
        val listParentsAsCardInfo = listValidParents.toElementCardSimpleInfo()

        _listSubElements.value = if(idChangedElement != 0L) {
            listParentsAsCardInfo.trySetBlockChangedElement(typeChangedElement, idChangedElement)
        } else {
            listParentsAsCardInfo
        }

        _infoElementLocation.value = ElementParam(typeElementLocation, idElementLocation)

        _locationAsElement.value = locationAsElement

        _iconIdLocation.value = getIconIdElement(typeElementLocation)

        if(locationAsElement is IElement)
            _showBackLocation.value = true
    }

    override suspend fun provideActions(button: TypeAction) {
        when(button) {
            TypeAction.ACTION_SAVE -> handleSave()
            TypeAction.ACTION_NAV_BACK -> handleBack()
            else -> {}
        }
    }

    private fun List<IElement>.toElementCardSimpleInfo(): List<ElementCardSimpleInfo> {
        val getTypeElementUseCase = GetTypeElementUseCase()

        return this.map { element ->
            val typeElement = getTypeElementUseCase(element)

            ElementCardSimpleInfo(
                idElement = element.id,
                typeElement = typeElement,
                name = element.name,
                block = false,
                iconId = getIconIdElement(typeElement)
            )
        }
    }

    private fun List<ElementCardSimpleInfo>.trySetBlockChangedElement(typeChangeElement: TypeElement, idChangedElement: Long): List<ElementCardSimpleInfo> {
        return this.map { element ->
            if(typeChangeElement == element.typeElement && idChangedElement == element.idElement) {
                element.copy(block = true)
            } else {
                element
            }
        }
    }

    private fun handleSave() {
        //val temp = LocationParam(_typeElementLocation.value!!, _idElementLocation.value!!)
        _infoClickSave.value = null
        _infoClickSave.value = _infoElementLocation.value
    }

    fun backLocationClick() {
        if (_infoClickSubElement.value == null) {

            val locationAsElement = _locationAsElement.value as IElement

            val idLocation = locationAsElement.idLocation
            val typeLocation = locationAsElement.typeLocation

            _infoClickSubElement.value = ElementParam(typeLocation, idLocation)
        }
    }

    fun elementClick(type: TypeElement, id: Long) {
        if (_infoClickSubElement.value == null) {
            _infoClickSubElement.value = ElementParam(type, id)
        }
    }
}