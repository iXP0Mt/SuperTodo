package com.ixp0mt.supertodo.presentation.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.TurnCompleteTaskUseCase
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


open class ElementViewModel(
    private val getFoldersByLocationUseCase: GetFoldersByLocationUseCase,
    private val getProjectsByLocationUseCase: GetProjectsByLocationUseCase,
    private val getTasksByLocationUseCase: GetTasksByLocationUseCase,
    private val deleteElementUseCase: DeleteElementUseCase,
    private val turnCompleteTaskUseCase: TurnCompleteTaskUseCase
) : ViewModel() {

    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _editClick = MutableLiveData<Boolean>(false)
    val editClick: LiveData<Boolean> = _editClick

    private val _elementClickInfo = MutableLiveData<ElementParam?>(null)
    val elementClickInfo: LiveData<ElementParam?> = _elementClickInfo

    private val _showDialogDelete = MutableLiveData<Boolean>(false)
    val showDialogDelete: LiveData<Boolean> = _showDialogDelete

    private val _listInternalFolders = MutableLiveData<List<FolderInfo>>(emptyList())
    val listInternalFolders: LiveData<List<FolderInfo>> = _listInternalFolders

    private val _listInternalProjects = MutableLiveData<List<ProjectInfo>>(emptyList())
    val listInternalProjects: LiveData<List<ProjectInfo>> = _listInternalProjects

    private val _listInternalTasks = MutableLiveData<List<TaskInfo>>(emptyList())
    val listInternalTasks: LiveData<List<TaskInfo>> = _listInternalTasks

    private lateinit var job: Job


    fun clearScreenState() {
        _elementClickInfo.value = null
        _editClick.value = false
        _showDialogDelete.value = false
        _backClick.value = null
        closeStates()
    }

    fun initScreen(screenState: ScreenState) {
        val screen = getScreen(screenState)
        screen?.let {
            val idElement = getIdElementFromArgs(screenState)
            idElement?.let {
                job = viewModelScope.launch {
                    initElement(idElement)
                    checkAction(screen, this)
                }
            }
        }
    }


    suspend fun getListInternalFolders(param: LocationParam) {
        val result = getFoldersByLocationUseCase.execute(param)
        when {
            result.isSuccess -> {
                _listInternalFolders.value = result.getOrThrow()
            }

            result.isFailure -> {}
        }
    }

    suspend fun getListInternalProjects(param: LocationParam) {
        val result = getProjectsByLocationUseCase.execute(param)
        when {
            result.isSuccess -> {
                _listInternalProjects.value = result.getOrThrow()
            }

            result.isFailure -> {}
        }
    }

    suspend fun getListInternalTasks(param: LocationParam) {
        val result = getTasksByLocationUseCase.execute(param)
        when {
            result.isSuccess -> {
                _listInternalTasks.value = result.getOrThrow()
            }

            result.isFailure -> {}
        }
    }


    protected open fun getScreen(screenState: ScreenState): Screen? = null
    protected open fun getIdElementFromArgs(screenState: ScreenState): Long? = null
    protected open suspend fun initElement(idElement: Long) {}
    protected open fun checkAction(screen: Screen, scope: CoroutineScope) {}


    protected fun deleteElement(element: ElementInfo) {
        viewModelScope.launch {
            val result = deleteElementUseCase.execute(element)
            when {
                result.isSuccess -> {
                    val responseDelete = result.getOrThrow()
                    Log.d("ttt", "numDeletedFolders = ${responseDelete.numDeletedFolders}")
                    Log.d("ttt", "numDeletedProjects = ${responseDelete.numDeletedProjects}")
                    Log.d("ttt", "numDeletedTasks = ${responseDelete.numDeletedTasks}")
                    handleBack()
                }

                result.isFailure -> {}
            }
        }
    }

    fun cancelDialogDelete() {
        _showDialogDelete.value = false
    }

    fun elementClick(type: TypeElement, id: Long) {
        if (_elementClickInfo.value == null) {
            _elementClickInfo.value = ElementParam(type, id)
        }
    }

    fun handleBack() {
        _backClick.value = _backClick.value?.not() ?: true
    }

    protected fun handleEdit() {
        _editClick.value = true
    }

    protected fun handleDelete() {
        _showDialogDelete.value = true
    }

    private fun closeStates() {
        job.cancel()
    }

    fun elementMainClick(element: ElementInfo) {
        when (element) {
            is TaskInfo -> {
                handleCompleteTask(element)
            }

            else -> {}
        }
    }


    private fun handleCompleteTask(task: TaskInfo) {
        viewModelScope.launch {
            val result = turnCompleteTaskUseCase.execute(task)
            when {
                result.isSuccess -> {
                    val newDateCompleted = result.getOrNull()
                    val updatedListTasks = _listInternalTasks.value?.map { lTask ->
                        if (lTask.idTask == task.idTask) {
                            lTask.copy(dateCompleted = newDateCompleted)
                        } else {
                            lTask
                        }
                    } ?: emptyList()
                    _listInternalTasks.value = updatedListTasks
                }

                result.isFailure -> {}
            }
        }
    }
}