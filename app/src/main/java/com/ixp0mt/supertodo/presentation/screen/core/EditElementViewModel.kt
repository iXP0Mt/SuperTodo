package com.ixp0mt.supertodo.presentation.screen.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.element.GetElementByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFolderByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.SaveEditFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.project.SaveEditProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.task.SaveEditTaskUseCase
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.util.TypeAction
import kotlinx.coroutines.launch

abstract class EditElementViewModel(
    getFolderByIdUseCase: GetFolderByIdUseCase? = null,
    getProjectByIdUseCase: GetProjectByIdUseCase? = null,
    getTaskByIdUseCase: GetTaskByIdUseCase? = null,
    private val saveEditFolderUseCase: SaveEditFolderUseCase? = null,
    private val saveEditProjectUseCase: SaveEditProjectUseCase? = null,
    private val saveEditTaskUseCase: SaveEditTaskUseCase? = null,
    private val getElementByLocationUseCase: GetElementByLocationUseCase
) : BaseViewModel(
    getFolderByIdUseCase = getFolderByIdUseCase,
    getProjectByIdUseCase = getProjectByIdUseCase,
    getTaskByIdUseCase = getTaskByIdUseCase
) {
    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _locationClickInfo = MutableLiveData<LocationParam?>(null)
    val locationClickInfo: LiveData<LocationParam?> = _locationClickInfo

    private val _showKeyboard = MutableLiveData<Boolean>(true)
    val showKeyboard: LiveData<Boolean> = _showKeyboard

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _elementInfo = MutableLiveData<ElementInfo?>(null)
    //val elementInfo: LiveData<ElementInfo?> = _elementInfo

    private val _typeElement = MutableLiveData<TypeElement?>(null)

    private val _textElement = MutableLiveData<String>("")
    val textElement: LiveData<String> = _textElement

    private val _descElement = MutableLiveData<String>("")
    val descElement: LiveData<String> = _descElement

    private val _locationInfo = MutableLiveData<LocationParam?>(null)

    private val _locationName = MutableLiveData<String>("")
    val locationName: LiveData<String> = _locationName

    private val _idIcon = MutableLiveData<Int>(null)
    val idIcon: LiveData<Int> = _idIcon


    override fun clearScreen() {
        super.clearScreen()

        _backClick.value = null
        _locationClickInfo.value = null
        _showKeyboard.value = false
        _errorMsg.value = null
    }

    override fun initScreen(screenState: ScreenState) {
        super.initScreen(screenState)

        initElement(screenState)
    }

    private fun initElement(screenState: ScreenState) {
        val idElement = screenState.currentArgs["ID"]?.toLongOrNull()
        if (idElement === null) return

        val route = screenState.currentScreen?.route?.rawRoute
        if (route === null) return

        val typeElement = TypeElement.convert(route)
        if (typeElement === null) return

        viewModelScope.launch {
            val element = loadElement(typeElement, idElement)
            if (element === null) return@launch

            _elementInfo.value = element

            _typeElement.value = typeElement

            _textElement.value = element.name
            _descElement.value = element.description

            val location = getLocationFromSavedStateHandle(screenState)
            _locationInfo.value = location ?: LocationParam(element.typeLocation, element.idLocation)

            _locationName.value = getNameLocation(_locationInfo.value!!.typeLocation, _locationInfo.value!!.idLocation)
            _idIcon.value = getIconIdLocation(_locationInfo.value!!.typeLocation)
        }
    }

    private fun getLocationFromSavedStateHandle(screenState: ScreenState): LocationParam? {
        val typeLocation: TypeLocation? = screenState.savedStateHandle["typeLocation"]
        val idLocation: Long? = screenState.savedStateHandle["idLocation"]

        if (typeLocation != null && idLocation != null) {
            return LocationParam(typeLocation, idLocation)
        }

        return null
    }

    /**
     * Обработка действий TopBar
     */
    protected fun handleAction(button: TypeAction) {
        when (button) {
            TypeAction.ACTION_NAV_BACK -> handleBack()
            TypeAction.ACTION_SAVE -> handleSaveEdit()
            else -> Unit
        }
    }

    private fun handleSaveEdit() {
        if (!handleValid()) return

        val typeElement = _typeElement.value!!

        val newElement = prepareElementToSave(typeElement) ?: return

        viewModelScope.launch {
            val result = saveEditElement(newElement)
            if(!result) return@launch

            hideKeyboard()
            handleBack()
        }
    }

    private fun handleValid(): Boolean {
        if (_textElement.value!!.isBlank()) {
            setErrorMsg("Пустое название")
            return false
        }
        return true
    }

    private fun prepareElementToSave(typeElement: TypeElement): ElementInfo? {
        return when (typeElement) {
            TypeElement.FOLDER -> (_elementInfo.value as FolderInfo).copy(
                name = _textElement.value!!,
                description = _descElement.value,
                typeLocation = _locationInfo.value!!.typeLocation,
                idLocation = _locationInfo.value!!.idLocation,
                dateEdit = System.currentTimeMillis()
            )
            TypeElement.PROJECT -> (_elementInfo.value as ProjectInfo).copy(
                name = _textElement.value!!,
                description = _descElement.value,
                typeLocation = _locationInfo.value!!.typeLocation,
                idLocation = _locationInfo.value!!.idLocation,
                dateEdit = System.currentTimeMillis()
            )
            TypeElement.TASK -> (_elementInfo.value as TaskInfo).copy(
                name = _textElement.value!!,
                description = _descElement.value,
                typeLocation = _locationInfo.value!!.typeLocation,
                idLocation = _locationInfo.value!!.idLocation,
                dateEdit = System.currentTimeMillis()
            )
            else -> null
        }
    }

    private suspend fun saveEditElement(element: ElementInfo): Boolean {
        return when (element) {
            is FolderInfo -> saveEditFolderUseCase?.let { saveEditFolder(element) } ?: false
            is ProjectInfo -> saveEditProjectUseCase?.let { saveEditProject(element) } ?: false
            is TaskInfo -> saveEditTaskUseCase?.let { saveEditTask(element) } ?: false
            else -> false
        }
    }

    /**
     * Сохраняет измененную задачу в базу данных
     *
     * @return true, если успешно сохранено, иначе false
     */
    private suspend fun saveEditFolder(folder: FolderInfo): Boolean {
        val result = saveEditFolderUseCase!!(folder)
        when {
            result.isSuccess -> {
                val valueEditRows = result.getOrThrow()
                if(valueEditRows > 0) return true
            }

            result.isFailure -> {
                setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
        return false
    }

    /**
     * Сохраняет измененную задачу в базу данных
     *
     * @return true, если успешно сохранено, иначе false
     */
    private suspend fun saveEditProject(project: ProjectInfo): Boolean {
        val result = saveEditProjectUseCase!!(project)
        when {
            result.isSuccess -> {
                val valueEditRows = result.getOrThrow()
                if(valueEditRows > 0) return true
            }

            result.isFailure -> {
                setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
        return false
    }

    /**
     * Сохраняет измененную задачу в базу данных
     *
     * @return true, если успешно сохранено, иначе false
     */
    private suspend fun saveEditTask(task: TaskInfo): Boolean {
        val result = saveEditTaskUseCase!!(task)
        when {
            result.isSuccess -> {
                val valueEditRows = result.getOrThrow()
                if(valueEditRows > 0) return true
            }

            result.isFailure -> {
                setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
        return false
    }

    fun handleBack() {
        _backClick.value = _backClick.value?.not() ?: true
    }

    private fun setErrorMsg(error: String?) {
        if(error == null) _errorMsg.value = "Unknown Error"
        else _errorMsg.value = error
    }

    fun clearErrorMsg() {
        _errorMsg.value = null
    }

    /**
     * Меняет текст элемента в соответствии с ограничениями
     */
    fun changeTextElement(inputString: String) {
        val typeElement = _typeElement.value
        val lenText = inputString.length
        when (typeElement) {
            TypeElement.FOLDER -> if (lenText > SettingConstant.MAX_FOLDER_NAME_LENGTH) return
            TypeElement.PROJECT -> if (lenText > SettingConstant.MAX_PROJECT_NAME_LENGTH) return
            TypeElement.TASK -> if (lenText > SettingConstant.MAX_TASK_NAME_LENGTH) return
            else -> if (lenText > SettingConstant.MAX_DEFAULT_LENGTH) return
        }

        _textElement.value = inputString
    }

    /**
     * Меняет описание элемента в соответствии с ограничениями
     */
    fun changeDescElement(inputString: String) {
        val typeElement = _typeElement.value
        val lenText = inputString.length
        when (typeElement) {
            TypeElement.FOLDER -> if (lenText > SettingConstant.MAX_FOLDER_DESCRIPTION_LENGTH) return
            TypeElement.PROJECT -> if (lenText > SettingConstant.MAX_PROJECT_DESCRIPTION_LENGTH) return
            TypeElement.TASK -> if (lenText > SettingConstant.MAX_TASK_DESCRIPTION_LENGTH) return
            else -> if (lenText > SettingConstant.MAX_DEFAULT_LENGTH) return
        }

        _descElement.value = inputString
    }

    private fun hideKeyboard() {
        _showKeyboard.value = false
    }

    private suspend fun getNameLocation(typeLocation: TypeLocation, idLocation: Long): String {
        val locationAsElement = getLocationAsElement(typeLocation, idLocation)
        return locationAsElement?.name ?: "NULL"
    }

    private suspend fun getLocationAsElement(typeLocation: TypeLocation, idLocation: Long): ElementInfo? {
        val param = LocationParam(typeLocation, idLocation)
        val result = getElementByLocationUseCase(param)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }
            result.isFailure -> {
            }
        }
        return null
    }

    fun onLocationClick() {
        if(_locationClickInfo.value != null) return

        if(_locationInfo.value == null) return

        _locationClickInfo.value = _locationInfo.value
    }
}