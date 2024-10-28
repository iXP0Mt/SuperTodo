package com.ixp0mt.supertodo.presentation.screen.task

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.TurnCompleteTaskUseCase
import com.ixp0mt.supertodo.presentation.navigation.Routes
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.ElementViewModel
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    getFoldersByLocationUseCase: GetFoldersByLocationUseCase,
    getProjectsByLocationUseCase: GetProjectsByLocationUseCase,
    getTasksByLocationUseCase: GetTasksByLocationUseCase,
    deleteElementUseCase: DeleteElementUseCase,
    turnCompleteTaskUseCase: TurnCompleteTaskUseCase
) : ElementViewModel(
    getFoldersByLocationUseCase = getFoldersByLocationUseCase,
    getProjectsByLocationUseCase = getProjectsByLocationUseCase,
    getTasksByLocationUseCase = getTasksByLocationUseCase,
    deleteElementUseCase = deleteElementUseCase,
    turnCompleteTaskUseCase = turnCompleteTaskUseCase
) {
    private val _taskInfo = MutableLiveData<TaskInfo>(TaskInfo.empty())

    override fun getScreen(screenState: ScreenState): Screen? =
        screenState.currentScreen as? Screen.Task

    override fun getIdElementFromArgs(screenState: ScreenState): Long? =
        screenState.currentArgs[Routes.Task.ID]?.toLongOrNull()

    override suspend fun initElement(idElement: Long) {
        initTask(idElement)
    }

    override fun checkAction(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.Task).buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_EDIT -> handleEdit()
                TypeAction.ACTION_DELETE -> handleDelete()
                else -> Unit
            }
        }.launchIn(scope)
    }

    private suspend fun initTask(idTask: Long) {
        if (getTaskInfo(idTask)) {
            val param = LocationParam(TypeLocation.TASK, idTask)
            getListInternalFolders(param)
        }
    }

    private suspend fun getTaskInfo(idTask: Long): Boolean {
        val param = GetTaskByIdParam(idTask)
        val result = getTaskByIdUseCase.execute(param)
        when {
            result.isSuccess -> {
                _taskInfo.value = result.getOrThrow()
                return true
            }

            result.isFailure -> {
                val e = result.exceptionOrNull()
                Log.d("ttt", "e: $e !!!")
                return false
            }
        }
        return false
    }

    fun deleteCurrentElement() {
        deleteElement(_taskInfo.value!!)
    }
}
/*
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val getTasksByLocationUseCase: GetTasksByLocationUseCase,
    private val deleteElementUseCase: DeleteElementUseCase
) : ViewModel() {

    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _editClick = MutableLiveData<Boolean>(false)
    val editClick: LiveData<Boolean> = _editClick

    private val _elementClickInfo = MutableLiveData<ElementParam?>(null)
    val elementClickInfo: LiveData<ElementParam?> = _elementClickInfo

    private val _taskInfo = MutableLiveData<TaskInfo>(TaskInfo.empty())

    private val _listInternalTasks = MutableLiveData<List<TaskInfo>>(emptyList())
    val listInternalTasks: LiveData<List<TaskInfo>> = _listInternalTasks

    private val _showDialogDelete = MutableLiveData<Boolean>(false)
    val showDialogDelete: LiveData<Boolean> = _showDialogDelete

    private lateinit var job: Job


    fun clearScreenState() {
        _elementClickInfo.value = null
        _editClick.value = false
        _showDialogDelete.value = false
        _backClick.value = null
        closeStates()
    }

    fun initScreen(screenState: ScreenState) {
        val screen = screenState.currentScreen as? Screen.Task
        screen?.let {
            val idTask = screenState.currentArgs[Routes.Task.ID]?.toLongOrNull()
            idTask?.let {
                job = viewModelScope.launch {
                    initTask(it)
                    checkAction(screen, this)
                    screenState.isNeedInit = false
                }
            }
        }
    }

    private suspend fun initTask(idTask: Long) {
        if (getTaskInfo(idTask)) {
            getListInternalTasks(idTask)
        }
    }

    private fun checkAction(screen: Screen.Task, scope: CoroutineScope) {
        screen.buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> {
                    handleBack()
                }

                TypeAction.ACTION_EDIT -> {
                    _editClick.value = true
                }

                TypeAction.ACTION_DELETE -> _showDialogDelete.value = true
                else -> Unit
            }
        }.launchIn(scope)
    }

    private suspend fun getTaskInfo(idTask: Long): Boolean {
        val params = GetTaskByIdParam(idTask)
        val result = getTaskByIdUseCase.execute(params)
        when {
            result.isSuccess -> {
                _taskInfo.value = result.getOrThrow()
                return true
            }

            result.isFailure -> {
                val e = result.exceptionOrNull()
                Log.d("ttt", "e: $e !!!")
                return false
            }
        }
        return false
    }

    private suspend fun getListInternalTasks(idTask: Long) {
        val param = LocationParam(
            typeLocation = TypeLocation.TASK,
            idLocation = idTask
        )
        val result = getTasksByLocationUseCase.execute(param)
        when {
            result.isSuccess -> {
                _listInternalTasks.value = result.getOrThrow()
            }

            result.isFailure -> {
                val e = result.exceptionOrNull()
                Log.d("ttt", "e: $e !!!")
            }
        }
    }

    fun deleteCurrentTask() {
        viewModelScope.launch {
            val task = _taskInfo.value!!
            val result = deleteElementUseCase.execute(task)
            when {
                result.isSuccess -> {
                    val responseDelete = result.getOrThrow()
                    Log.d("ttt", "numDeletedFolders = ${responseDelete.numDeletedFolders}")
                    Log.d("ttt", "numDeletedProjects = ${responseDelete.numDeletedProjects}")
                    Log.d("ttt", "numDeletedTasks = ${responseDelete.numDeletedTasks}")
                    handleBack()
                }

                result.isFailure -> {
                    val e = result.exceptionOrNull()
                    Log.d("ttt", "e: $e !!!")
                }
            }
        }
    }

    fun elementClick(type: TypeElement, id: Long) {
        if (_elementClickInfo.value == null) {
            _elementClickInfo.value = ElementParam(type, id)
        }
    }

    fun cancelDialogDelete() { _showDialogDelete.value = false }
    fun handleBack() { _backClick.value = _backClick.value?.not() ?: true }
    private fun closeStates() { job.cancel() }
}*/
