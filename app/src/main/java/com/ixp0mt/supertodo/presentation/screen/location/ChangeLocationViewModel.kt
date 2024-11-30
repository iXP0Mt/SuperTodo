package com.ixp0mt.supertodo.presentation.screen.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.element.GetElementByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.presentation.navigation.Routes
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.BaseViewModel
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLocationViewModel @Inject constructor(
    getFoldersByLocationUseCase: GetFoldersByLocationUseCase,
    getProjectsByLocationUseCase: GetProjectsByLocationUseCase,
    getTasksByLocationUseCase: GetTasksByLocationUseCase,
    private val getElementByLocationUseCase: GetElementByLocationUseCase
) : BaseViewModel(
    getFoldersByLocationUseCase = getFoldersByLocationUseCase,
    getProjectsByLocationUseCase = getProjectsByLocationUseCase,
    getTasksByLocationUseCase = getTasksByLocationUseCase
) {
    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _saveClickInfo = MutableLiveData<LocationParam?>(null)
    val saveClickInfo: LiveData<LocationParam?> = _saveClickInfo

    private val _idIcon = MutableLiveData<Int>(null)
    val idIcon: LiveData<Int> = _idIcon

    private val _elementClickInfo = MutableLiveData<LocationParam?>(null)
    val elementClickInfo: LiveData<LocationParam?> = _elementClickInfo

    private val _showBackLocation = MutableLiveData<Boolean>(false)
    val showBackLocation: LiveData<Boolean> = _showBackLocation

    private val _elementOfLocation = MutableLiveData<ElementInfo?>(null)
    val elementOfLocation: LiveData<ElementInfo?> = _elementOfLocation

    private val _typeElementLocation = MutableLiveData<TypeLocation?>(null)

    private val _idElementLocation = MutableLiveData<Long?>(null)

    val listFolders: LiveData<List<FolderInfo>> = _listFolders
    val listProjects: LiveData<List<ProjectInfo>> = _listProjects
    val listTasks: LiveData<List<TaskInfo>> = _listTasks


    override fun provideScreen(screenState: ScreenState): Screen {
        return screenState.currentScreen as Screen.ChangeLocation
    }

    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.ChangeLocation).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }

    /**
     * Обработка действий TopBar
     */
    private fun handleAction(button: TypeAction) {
        when (button) {
            TypeAction.ACTION_NAV_BACK -> handleBack()
            TypeAction.ACTION_SAVE -> handleSave()
            else -> Unit
        }
    }

    override fun clearScreen() {
        super.clearScreen()

        _backClick.value = null
        _saveClickInfo.value = null
        _elementClickInfo.value = null
    }

    override fun initScreen(screenState: ScreenState) {
        super.initScreen(screenState)

        initElement(screenState)
    }

    private fun initElement(screenState: ScreenState) {
        val idElementLocation = screenState.currentArgs[Routes.ChangeLocation.ID]?.toLongOrNull()
        if(idElementLocation === null) return

        val typeElementLocation = screenState.currentArgs[Routes.ChangeLocation.TYPE]?.let { TypeLocation.convert(it) }
        if(typeElementLocation === null) return

        val idChangedElement: Long = screenState.previousArgs["ID"]?.toLongOrNull() ?: 0
        //if(idChangedElement === null) return
        // Если ID элемента нет, значит элемент создаётся в данный момент

        val typeChangedElement = screenState.previousRawRoute?.let { TypeElement.convert(it) }
        if(typeChangedElement === null) return

        viewModelScope.launch {
            val elementLocation = getElementByLocation(typeElementLocation, idElementLocation) ?: return@launch

            val typeLocationAsTypeElement = TypeElement.convert(typeElementLocation.name) ?: return@launch

            val flags = if(typeChangedElement == TypeElement.TASK) 0b111
            else 0b110

            loadInternalElements(typeLocationAsTypeElement, idElementLocation, flags)
            if(idChangedElement != 0L) markChangedElement(typeChangedElement, idChangedElement)

            _elementOfLocation.value = elementLocation
            _typeElementLocation.value = typeElementLocation
            _idElementLocation.value = idElementLocation
            _idIcon.value = getIconIdLocation(typeElementLocation)
            _showBackLocation.value = true
        }
    }

    /**
     * Попробовать отметить элемент в списке внутренних элементов текущей локации, который сейчас редактируется.
     * Метка ставится таким образом: параметр элемента dateArchive становится равным 1.
     */
    private fun markChangedElement(typeChangedElement: TypeElement, idChangedElement: Long) {
        when(typeChangedElement) {
            TypeElement.FOLDER -> {
                val targetIndex = _listFolders.value!!.indexOfFirst { it.idFolder == idChangedElement }
                _listFolders.value = if(targetIndex == -1) return
                else _listFolders.value!!.mapIndexed { index, element -> if(index == targetIndex) element.copy(dateArchive = 1L) else element }
            }
            TypeElement.PROJECT -> {
                val targetIndex = _listProjects.value!!.indexOfFirst { it.idProject == idChangedElement }
                _listProjects.value = if(targetIndex == -1) return
                else _listProjects.value!!.mapIndexed { index, element -> if(index == targetIndex) element.copy(dateArchive = 1L) else element }
            }
            TypeElement.TASK -> {
                val targetIndex = _listTasks.value!!.indexOfFirst { it.idTask == idChangedElement }
                _listTasks.value = if(targetIndex == -1) return
                else _listTasks.value!!.mapIndexed { index, element -> if(index == targetIndex) element.copy(dateArchive = 1L) else element }
            }
            else -> return
        }
    }

    /**
     * Получает элемент локации
     */
    private suspend fun getElementByLocation(typeLocation: TypeLocation, idLocation: Long): ElementInfo? {
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

    fun handleBack() {
        _backClick.value = _backClick.value?.not() ?: true
    }

    private fun handleSave() {
        val temp = LocationParam(_typeElementLocation.value!!, _idElementLocation.value!!)
        _saveClickInfo.value = null
        _saveClickInfo.value = temp
    }

    fun backLocationClick() {
        if (_elementClickInfo.value == null) {

            val idLocation = _elementOfLocation.value!!.idLocation
            val typeLocation = _elementOfLocation.value!!.typeLocation

            //if(typeLocation == TypeLocation.MAIN) idLocation = 0L

            _elementClickInfo.value = LocationParam(typeLocation, idLocation)
        }
    }

    fun elementClick(type: TypeElement, id: Long) {
        if (_elementClickInfo.value == null) {
            _elementClickInfo.value = LocationParam(TypeLocation.convert(type.name)!!, id)
        }
    }
}