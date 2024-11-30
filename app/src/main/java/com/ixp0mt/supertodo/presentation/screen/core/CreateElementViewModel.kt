package com.ixp0mt.supertodo.presentation.screen.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.element.GetElementByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.SaveNewFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.project.SaveNewProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.task.SaveNewTaskUseCase
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.util.TypeAction
import kotlinx.coroutines.launch

abstract class CreateElementViewModel(
    private val saveNewFolderUseCase: SaveNewFolderUseCase? = null,
    private val saveNewProjectUseCase: SaveNewProjectUseCase? = null,
    private val saveNewTaskUseCase: SaveNewTaskUseCase? = null,
    private val getElementByLocationUseCase: GetElementByLocationUseCase
) : BaseViewModel(

) {
    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _showKeyboard = MutableLiveData<Boolean?>(true)
    val showKeyboard: LiveData<Boolean?> = _showKeyboard

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _paramNewElement = MutableLiveData<ElementParam?>(null)
    val paramNewElement: LiveData<ElementParam?> = _paramNewElement

    private val _textElement = MutableLiveData<String>("")
    val textElement: LiveData<String> = _textElement

    private val _descElement = MutableLiveData<String>("")
    val descElement: LiveData<String> = _descElement

    private val _locationInfo = MutableLiveData<LocationParam?>(null)

    private val _typeCreatedElement = MutableLiveData<TypeElement?>(null)

    private val _locationName = MutableLiveData<String>("")
    val locationName: LiveData<String> = _locationName

    private val _idIcon = MutableLiveData<Int>(null)
    val idIcon: LiveData<Int> = _idIcon

    private val _locationClickInfo = MutableLiveData<LocationParam?>(null)
    val locationClickInfo: LiveData<LocationParam?> = _locationClickInfo


    override fun clearScreen() {
        super.clearScreen()

        _backClick.value = null
        _showKeyboard.value = null
        _errorMsg.value = null
        _paramNewElement.value = null
        _locationClickInfo.value = null
    }

    override fun initScreen(screenState: ScreenState) {
        super.initScreen(screenState)

        initCreatedElement(screenState)
    }

    private fun initCreatedElement(screenState: ScreenState) {
        val typeCreatedElement= getTypeCreatedElement(screenState) ?: return
        val locationInfo = getTempLocation(screenState) ?: return

        viewModelScope.launch {
            _locationName.value = getNameLocation(locationInfo.typeLocation, locationInfo.idLocation)
            _idIcon.value = getIconIdLocation(locationInfo.typeLocation)
        }

        _typeCreatedElement.value = typeCreatedElement
        _locationInfo.value = locationInfo
    }

    /**
     * Инициализирует тип создаваемого элемента
     */
    private fun getTypeCreatedElement(screenState: ScreenState): TypeElement? {
        val screen = screenState.currentScreen
        return when(screen) {
            is Screen.FolderCreate -> TypeElement.FOLDER
            is Screen.ProjectCreate -> TypeElement.PROJECT
            is Screen.TaskCreate -> TypeElement.TASK
            else -> null
        }
    }

    /**
     * Инициализирует будущую локацию создаваемого элемента
     */
    private fun getTempLocation(screenState: ScreenState): LocationParam? {
        return getLocationFromSavedStateHandle(screenState)
            ?: getLocationFromPreviousRoute(screenState)
    }

    /**
     * Пробует получить параметры локации из SavedStateHandle состояния экрана
     *
     * @return LocationParam, если удалось получить параметры, иначе null
     */
    private fun getLocationFromSavedStateHandle(screenState: ScreenState): LocationParam? {
        val typeLocation: TypeLocation? = screenState.savedStateHandle["typeLocation"]
        val idLocation: Long? = screenState.savedStateHandle["idLocation"]

        if (typeLocation != null && idLocation != null) {
            return LocationParam(typeLocation, idLocation)
        }

        return null
    }

    /**
     * Получает локацию из предыдущего маршрута для того, чтобы понимать какую локацию будет иметь создающийся элемент.
     */
    private fun getLocationFromPreviousRoute(screenState: ScreenState): LocationParam? {
        val prevRoute = screenState.previousRawRoute ?: return null

        val typeLocation = TypeLocation.convert(prevRoute) ?: return null

        val idLocation: Long = if (typeLocation == TypeLocation.MAIN) 0
        else screenState.previousArgs["ID"]?.toLongOrNull() ?: return null

        return LocationParam(typeLocation, idLocation)
    }

    /**
     * Обработка действий TopBar
     */
    protected fun handleAction(button: TypeAction) {
        when (button) {
            TypeAction.ACTION_NAV_BACK -> handleBack()
            TypeAction.ACTION_SAVE -> handleSave()
            else -> Unit
        }
    }

    /**
     * Обработка действия "сохранить" из TopBar
     */
    private fun handleSave() {
        if (!handleValid()) return

        val typeNewElement = _typeCreatedElement.value!!

        val newElement = prepareElementToSave(typeNewElement) ?: return

        viewModelScope.launch {
            val idNewElement = saveElement(newElement) ?: return@launch
            hideKeyboard()
            _paramNewElement.value = ElementParam(typeNewElement, idNewElement)
        }
    }

    /**
     * Проверяет валидность элемента перед сохранением
     *
     * @return true, если все данные корректны, иначе false
     */
    private fun handleValid(): Boolean {
        if (_textElement.value!!.isBlank()) {
            setErrorMsg("Пустое название")
            return false
        }
        return true
    }

    /**
     * Подготовить создаваемый объект для сохранения
     *
     * @param typeElement Тип создаваемого элемента
     *
     * @return Объект ElementInfo если удалось подготовить элемент, иначе null
     */
    private fun prepareElementToSave(typeElement: TypeElement): ElementInfo? {
        return when (typeElement) {
            TypeElement.FOLDER -> FolderInfo(
                idFolder = 0,
                name = _textElement.value!!,
                description = _descElement.value,
                typeLocation = _locationInfo.value!!.typeLocation,
                idLocation = _locationInfo.value!!.idLocation,
                dateCreate = System.currentTimeMillis(),
                dateEdit = null,
                dateArchive = null
            )

            TypeElement.PROJECT -> ProjectInfo(
                idProject = 0,
                name = _textElement.value!!,
                description = _descElement.value,
                typeLocation = _locationInfo.value!!.typeLocation,
                idLocation = _locationInfo.value!!.idLocation,
                dateCreate = System.currentTimeMillis(),
                dateEdit = null,
                dateArchive = null,
                dateStart = null,
                dateEnd = null,
                dateCompleted = null
            )

            TypeElement.TASK -> TaskInfo(
                idTask = 0,
                name = _textElement.value!!,
                description = _descElement.value,
                typeLocation = _locationInfo.value!!.typeLocation,
                idLocation = _locationInfo.value!!.idLocation,
                dateCreate = System.currentTimeMillis(),
                dateEdit = null,
                dateArchive = null,
                dateStart = null,
                dateEnd = null,
                dateCompleted = null
            )

            else -> null
        }
    }

    /**
     * Сохраняет создаваемый элемент в базу данных
     *
     * @return true, если успешно, иначе false
     */
    private suspend fun saveElement(element: ElementInfo): Long? {
        return when (element) {
            is FolderInfo -> saveNewFolderUseCase?.let { saveFolder(element) }
            is ProjectInfo -> saveNewProjectUseCase?.let { saveProject(element) }
            is TaskInfo -> saveNewTaskUseCase?.let { saveTask(element) }
            else -> null
        }
    }

    /**
     * Сохраняет папку в базу данных
     *
     * @return ID новой папки, если создано успешно, иначе null
     */
    private suspend fun saveFolder(folder: FolderInfo): Long? {
        val result = saveNewFolderUseCase!!(folder)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }

            result.isFailure -> {
                setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
        return null
    }

    /**
     * Сохраняет проект в базу данных
     *
     * @return ID нового проекта, если создано успешно, иначе null
     */
    private suspend fun saveProject(project: ProjectInfo): Long? {
        val result = saveNewProjectUseCase!!(project)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }

            result.isFailure -> {
                setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
        return null
    }

    /**
     * Сохраняет задачу в базу данных
     *
     * @return ID новой задачи, если создано успешно, иначе null
     */
    private suspend fun saveTask(task: TaskInfo): Long? {
        val result = saveNewTaskUseCase!!(task)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }

            result.isFailure -> {
                setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
        return null
    }

    /**
     * Обработка действия "назад" из TopBar
     */
    fun handleBack() {
        hideKeyboard()
        _backClick.value = _backClick.value?.not() ?: true
    }

    /**
     * Задаёт отображаемую ошибку посредством SnackBar
     */
    private fun setErrorMsg(error: String?) {
        if(error == null) _errorMsg.value = "Unknown Error"
        else _errorMsg.value = error
    }

    /**
     * Очищает отображаемую ошибку (SnackBar)
     */
    fun clearErrorMsg() {
        _errorMsg.value = null
    }

    /**
     * Меняет текст элемента в соответствии с ограничениями
     */
    fun changeTextElement(inputString: String) {
        val typeElement = _typeCreatedElement.value
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
        val typeElement = _typeCreatedElement.value
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
        hideKeyboard()

        if(_locationClickInfo.value != null) return

        if(_locationInfo.value == null) return

        _locationClickInfo.value = _locationInfo.value
    }
}