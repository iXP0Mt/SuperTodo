package com.ixp0mt.supertodo.presentation.screen.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.R
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
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLocationViewModel @Inject constructor(
    private val getFoldersByLocationUseCase: GetFoldersByLocationUseCase,
    private val getProjectsByLocationUseCase: GetProjectsByLocationUseCase,
    private val getTasksByLocationUseCase: GetTasksByLocationUseCase,
    private val getElementByLocationUseCase: GetElementByLocationUseCase
) : ViewModel() {
    private val _elementOfLocation = MutableLiveData<ElementInfo>(FolderInfo.empty())
    val elementOfLocation: LiveData<ElementInfo> = _elementOfLocation

    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _saveClickInfo = MutableLiveData<LocationParam?>(null)
    val saveClickInfo: LiveData<LocationParam?> = _saveClickInfo

    private val _typeElementChange = MutableLiveData<TypeElement?>(null)
    val typeElementChange: LiveData<TypeElement?> = _typeElementChange

    private val _idElementChange = MutableLiveData<Long>(0L)
    val idElementChange: LiveData<Long> = _idElementChange

    private val _elementClickInfo = MutableLiveData<LocationParam?>(null)
    val elementClickInfo: LiveData<LocationParam?> = _elementClickInfo

    private val _listInternalFolders = MutableLiveData<List<FolderInfo>>(emptyList())
    val listInternalFolders: LiveData<List<FolderInfo>> = _listInternalFolders

    private val _listInternalProjects = MutableLiveData<List<ProjectInfo>>(emptyList())
    val listInternalProjects: LiveData<List<ProjectInfo>> = _listInternalProjects

    private val _listInternalTasks = MutableLiveData<List<TaskInfo>>(emptyList())
    val listInternalTasks: LiveData<List<TaskInfo>> = _listInternalTasks

    private val _typeLocation = MutableLiveData<TypeLocation>(TypeLocation.MAIN)

    private val _idLocation = MutableLiveData<Long>(0L)

    private val _showBackLocation = MutableLiveData<Boolean>(false)
    val showBackLocation: LiveData<Boolean> = _showBackLocation



    private var isElementFound: Boolean = false

    private lateinit var job: Job

    fun clearScreenState() {
        _elementClickInfo.value = null
        _typeElementChange.value = null
        _idElementChange.value = 0
        _backClick.value = null
        _saveClickInfo.value = null
        _typeLocation.value = TypeLocation.MAIN
        _showBackLocation.value = false
        isElementFound = false
        _elementOfLocation.value = FolderInfo.empty()
        closeStates()
    }

    private fun closeStates() {
        job.cancel()
    }

    fun initScreen(screenState: ScreenState) {
        val screen = screenState.currentScreen as Screen.ChangeLocation
        screen.let {
            val lIdLocation = screenState.currentArgs[Routes.ChangeLocation.ID]?.toLongOrNull()
            if(lIdLocation === null) return

            val lTypeLocation = screenState.currentArgs[Routes.ChangeLocation.TYPE]?.let { TypeLocation.convert(it) }
            if(lTypeLocation === null) return

            val idElementChange = screenState.previousArgs["ID"]?.toLongOrNull()
            if(idElementChange === null) return
            // Если ID элемента нет, значит элемент создаётся в данный момент

            val typeElementChange = screenState.previousRawRoute?.let { TypeElement.convert(it) }
            if(typeElementChange === null) return

            _typeLocation.value = lTypeLocation

            _idLocation.value = lIdLocation

            if(lTypeLocation != TypeLocation.MAIN) _showBackLocation.value = true

            _idElementChange.value = idElementChange
            _typeElementChange.value = typeElementChange

            job = viewModelScope.launch {
                getElementByLocation(lTypeLocation, lIdLocation)
                //_nameLocation.value = getNameLocation(lTypeLocation, lIdLocation)

                val param = LocationParam(lTypeLocation, lIdLocation)

                getListInternalFolders(param)
                getListInternalProjects(param)
                if(typeElementChange == TypeElement.TASK) getListInternalTasks(param)

                checkAction(screen, this)
            }
        }
    }

    private suspend fun getElementByLocation(typeLocation: TypeLocation, idLocation: Long) {
        val param = LocationParam(typeLocation, idLocation)
        val result = getElementByLocationUseCase.execute(param)
        when {
            result.isSuccess -> {
                _elementOfLocation.value = result.getOrThrow()
            }
            result.isFailure -> {
                val e = result.exceptionOrNull()
                Log.d("ttt", "Exception: $e")
            }
        }
    }

/*    private suspend fun getNameLocation(typeLocation: TypeLocation, idLocation: Long): String {
        val param = LocationParam(typeLocation, idLocation)
        val result = getElementByLocationUseCase.execute(param)
        when {
            result.isSuccess -> {
                val element = result.getOrThrow()
                return element.name
            }
            result.isFailure -> {
                val e = result.exceptionOrNull()
                Log.d("ttt", "Exception: $e")
            }
        }
        return "NULL"
    }*/

    private suspend fun getListInternalFolders(param: LocationParam) {
        val result = getFoldersByLocationUseCase(param)
        when {
            result.isSuccess -> {
                val tempList = result.getOrThrow()
                if(!isElementFound && _typeElementChange.value == TypeElement.FOLDER) {
                    val targetIndex = tempList.indexOfFirst { it.idFolder == _idElementChange.value }

                    _listInternalFolders.value = if(targetIndex != -1) {
                        isElementFound = true
                        tempList.mapIndexed { index, element ->
                            // dateArchive - метка для редактируемого элемента
                            if(index == targetIndex) element.copy(dateArchive = 1L) else element
                        }
                    } else {
                        tempList
                    }
                } else {
                    _listInternalFolders.value = tempList
                }
            }

            result.isFailure -> {}
        }
    }

    private suspend fun getListInternalProjects(param: LocationParam) {
        val result = getProjectsByLocationUseCase(param)
        when {
            result.isSuccess -> {
                val tempList = result.getOrThrow()

                if(!isElementFound && _typeElementChange.value == TypeElement.PROJECT) {
                    val targetIndex = tempList.indexOfFirst { it.idProject == _idElementChange.value }

                    _listInternalProjects.value = if(targetIndex != -1) {
                        isElementFound = true
                        tempList.mapIndexed { index, element ->
                            if(index == targetIndex) element.copy(dateArchive = 1) else element
                        }
                    } else {
                        tempList
                    }
                } else {
                    _listInternalProjects.value = tempList
                }
            }

            result.isFailure -> {}
        }
    }

    private suspend fun getListInternalTasks(param: LocationParam) {
        val result = getTasksByLocationUseCase(param)
        when {
            result.isSuccess -> {
                val tempList = result.getOrThrow()

                if(!isElementFound && _typeElementChange.value == TypeElement.TASK) {
                    val targetIndex = tempList.indexOfFirst { it.idTask == _idElementChange.value }

                    _listInternalTasks.value = if(targetIndex != -1) {
                        isElementFound = true
                        tempList.mapIndexed { index, element ->
                            if(index == targetIndex) element.copy(dateArchive = 1) else element
                        }
                    } else {
                        tempList
                    }
                } else {
                    _listInternalTasks.value = tempList
                }
            }

            result.isFailure -> {}
        }
    }

    private fun checkAction(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.ChangeLocation).buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_SAVE -> handleSave()
                else -> Unit
            }
        }.launchIn(scope)
    }

    fun getIconLocation(): Int? {
        return when(_typeLocation.value) {
            TypeLocation.FOLDER -> R.drawable.ic_folder
            TypeLocation.PROJECT -> R.drawable.ic_project
            TypeLocation.TASK -> R.drawable.ic_task
            TypeLocation.MAIN -> R.drawable.ic_home
            else -> null
        }
    }

    fun elementClick(type: TypeElement, id: Long) {
        if (_elementClickInfo.value == null) {
            _elementClickInfo.value = LocationParam(TypeLocation.convert(type.name), id)
        }
    }

    fun backLocationClick() {
        Log.d("ttt","_elementClickInfo = ${_elementClickInfo.value}")
        if (_elementClickInfo.value == null) {

            var idLocation = _elementOfLocation.value!!.idLocation
            val typeLocation = _elementOfLocation.value!!.typeLocation

            if(typeLocation == TypeLocation.MAIN) idLocation = 0L

            idLocation?.let { _elementClickInfo.value = LocationParam(typeLocation, it) }
        }
    }


    fun handleBack() {
        _backClick.value = _backClick.value?.not() ?: true
    }

    private fun handleSave() {
        //_saveClick.value = _saveClick.value?.not() ?: true

        if(_saveClickInfo.value == null) {
            _saveClickInfo.value = LocationParam(_typeLocation.value!!, _idLocation.value!!)
        }
    }
}