package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.usecase.element.CreateElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetLocationAsElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetTypeElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.ValidElementUseCase
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.util.TypeAction

open class EditorElementViewModel(
    private val getElementUseCase: GetElementUseCase,
    private val validElementUseCase: ValidElementUseCase,
    private val getLocationAsElementUseCase: GetLocationAsElementUseCase,
    private val createElementUseCase: CreateElementUseCase,
    private val saveElementUseCase: SaveElementUseCase

) : BaseViewModel() {

    protected val _currentElement = MutableLiveData<IElement>()

    private val _infoLocation = MutableLiveData<ElementParam>()

    private val _textElement = MutableLiveData<String>("")
    val textElement: LiveData<String> = _textElement

    private val _descElement = MutableLiveData<String>("")
    val descElement: LiveData<String> = _descElement

    private val _iconIdLocation = MutableLiveData<Int>(null)
    val iconIdLocation: LiveData<Int> = _iconIdLocation

    private val _locationName = MutableLiveData<String>("")
    val locationName: LiveData<String> = _locationName

    private val _showKeyboard = MutableLiveData<Boolean?>(null)
    val showKeyboard: LiveData<Boolean?> = _showKeyboard

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _infoClickChangeLocation = MutableLiveData<ElementParam?>(null)
    val infoClickChangeLocation: LiveData<ElementParam?> = _infoClickChangeLocation

    private val _paramSavedElement = MutableLiveData<ElementParam?>(null)
    val paramSavedElement: LiveData<ElementParam?> = _paramSavedElement


    override fun clearData() {
        _showKeyboard.value = false
        _errorMsg.value = null
        _infoClickChangeLocation.value = null
        _paramSavedElement.value = null
    }


    override suspend fun initData(screenState: ScreenState) {

        val idElement: Long = screenState.currentArgs["ID"]?.toLongOrNull() ?: 0

        val currentElement = getElementUseCase(idElement) as IElement

        //val infoLocation: ElementParam = getTempLocation(screenState) ?: return

        val infoLocation = getLocationFromSavedStateHandle(screenState)
            ?: getLocationFromCurrentElement(currentElement)
            ?: getLocationFromPreviousRoute(screenState)
            ?: return

        val elementLocation = getLocationAsElementUseCase(infoLocation)

        _textElement.value = currentElement.name
        _descElement.value = currentElement.description ?: ""
        _iconIdLocation.value = getIconIdElement(infoLocation.typeElement)
        _locationName.value = elementLocation.name
        _currentElement.value = currentElement
        _infoLocation.value = infoLocation

        if(idElement == 0L)
            _showKeyboard.value = true
    }


    override suspend fun provideActions(button: TypeAction) {
        when (button) {
            TypeAction.ACTION_NAV_BACK -> handleBack()
            TypeAction.ACTION_SAVE -> handleSave()
            else -> Unit
        }
    }


    private fun getLocationFromCurrentElement(element: IElement): ElementParam? {
        if(element.typeLocation == TypeElement.DEFAULT) return null

        return ElementParam(element.typeLocation, element.idLocation)
    }

    /**
     * Пробует получить параметры локации из SavedStateHandle состояния экрана
     *
     * @return LocationParam, если удалось получить параметры, иначе null
     */
    private fun getLocationFromSavedStateHandle(screenState: ScreenState): ElementParam? {
        val typeLocation: TypeElement = screenState.savedStateHandle["typeLocation"] ?: return null
        val idLocation: Long = screenState.savedStateHandle["idLocation"] ?: return null

        return ElementParam(typeLocation, idLocation)
    }

    /**
     * Получает локацию из предыдущего маршрута.
     */
    private fun getLocationFromPreviousRoute(screenState: ScreenState): ElementParam? {
        val prevRoute = screenState.previousRawRoute ?: return null

        val typeLocation = TypeElement.convert(prevRoute)

        val idLocation: Long = screenState.previousArgs["ID"]?.toLongOrNull() ?: 0

        return ElementParam(typeLocation, idLocation)
    }

    private fun setErrorMsg(error: String?) {
        if(error == null) _errorMsg.value = "Unknown Error"
        else _errorMsg.value = error
    }

    fun clearErrorMsg() {
        _errorMsg.value = null
    }

    protected open fun prepareElementToSave(): IElement?  {
        if (!handleValid()) return null

        return createElementUseCase(
            _currentElement.value!!.id,
            _textElement.value!!,
            _descElement.value,
            _infoLocation.value!!.typeElement,
            _infoLocation.value!!.idElement,
            _currentElement.value!!.dateCreate,
            _currentElement.value!!.dateEdit,
            _currentElement.value!!.dateArchive,
        )
    }

    private suspend fun handleSave() {

        val element = prepareElementToSave() ?: return

        hideKeyboard()

        val idSavedElement = saveElementUseCase(element)
        val getTypeElementUseCase = GetTypeElementUseCase()
        val typeSavedElement = getTypeElementUseCase(_currentElement.value!!)
        _paramSavedElement.value = ElementParam(typeSavedElement, idSavedElement)
    }

    private fun handleValid(): Boolean {
        if (_textElement.value!!.isBlank()) {
            setErrorMsg("Пустое название")
            return false
        }
        return true
    }

    private fun hideKeyboard() {
        _showKeyboard.value = false
    }

    fun changeTextElement(str: String) {
        if(!validElementUseCase.validText(str)) return
        _textElement.value = str
    }

    fun changeDescElement(str: String) {
        if(!validElementUseCase.validDescription(str)) return
        _descElement.value = str
    }

    fun onLocationClick() {
        _infoClickChangeLocation.value = _infoLocation.value
    }

    override fun handleBack() {
        hideKeyboard()
        super.handleBack()
    }
}


/*{
    @Composable
    operator fun invoke(): ViewModel {
        return hiltViewModel()
    }
}*/

