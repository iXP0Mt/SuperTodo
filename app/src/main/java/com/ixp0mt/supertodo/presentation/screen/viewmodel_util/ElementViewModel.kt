package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.MarkElementCompleteParam
import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.model.element.IElementMeta
import com.ixp0mt.supertodo.domain.usecase.element.GetElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetStrCountersUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetSubElementsWithCountersUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetTypeElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.IsElementCompleteUseCase
import com.ixp0mt.supertodo.domain.usecase.element.MarkElementCompleteUseCase
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.util.ElementCardInfo
import kotlinx.coroutines.launch

abstract class ElementViewModel(
    private val getElementUseCase: GetElementUseCase,
    private val getSubElementsWithCountersUseCase: GetSubElementsWithCountersUseCase,
    private val markElementCompleteUseCase: MarkElementCompleteUseCase
) : BaseViewModel() {

    protected val _currentElement = MutableLiveData<IElementMeta>()
    val currentElement: LiveData<IElementMeta> = _currentElement

    private val _listSubElements = MutableLiveData<List<ElementCardInfo>>(emptyList())
    val listSubElements: LiveData<List<ElementCardInfo>> = _listSubElements

    private val _infoClickSubElement = MutableLiveData<ElementParam?>(null)
    val infoClickSubElement: LiveData<ElementParam?> = _infoClickSubElement


    override fun clearData() {
        _infoClickSubElement.value = null
    }

    override suspend fun initData(screenState: ScreenState) {
        val idElement = screenState.currentArgs["ID"]?.toLongOrNull() ?: 0
        val route = screenState.currentScreen.route.rawRoute
        val typeElement = TypeElement.convert(route)

        val currentElement = getElementUseCase(idElement)

        _listSubElements.value = getSubElementsWithCountersUseCase(typeElement, idElement).toElementCardInfo()
        _currentElement.value = currentElement
    }

    private fun List<IElement>.toElementCardInfo(): List<ElementCardInfo> {

        val getStrCountersUseCase = GetStrCountersUseCase()
        val isElementCompleteUseCase = IsElementCompleteUseCase()
        val getTypeElementUseCase = GetTypeElementUseCase()

        return this.map { element ->
            val typeElement = getTypeElementUseCase(element)

            ElementCardInfo(
                element.id,
                typeElement,
                getIconIdElement(typeElement),
                element.name,
                isElementCompleteUseCase(element),
                getStrCountersUseCase(element)
            )
        }
    }

    fun elementClick(typeElement: TypeElement, idElement: Long) {
        if (_infoClickSubElement.value == null) {
            _infoClickSubElement.value = ElementParam(typeElement, idElement)
        }
    }

    fun markElementComplete(typeElement: TypeElement, idElement: Long, isComplete: Boolean) {
        val param = MarkElementCompleteParam(typeElement, idElement, isComplete)
        viewModelScope.launch {
            if(markElementCompleteUseCase(param))
                updateMarkInSubListElements(typeElement, idElement, isComplete)
        }
    }

    private fun updateMarkInSubListElements(typeElement: TypeElement, idElement: Long, isComplete: Boolean) {
        _listSubElements.value = _listSubElements.value!!.map {
            if(it.typeElement == typeElement && it.id == idElement) {
                it.copy(isComplete = !isComplete)
            } else {
                it
            }
        }
    }
}