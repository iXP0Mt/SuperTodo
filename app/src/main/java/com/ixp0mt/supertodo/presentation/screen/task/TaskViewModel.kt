package com.ixp0mt.supertodo.presentation.screen.task

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetNamesFullLocationElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.MarkCompleteProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.MarkCompleteTaskUseCase
import com.ixp0mt.supertodo.presentation.navigation.Routes
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.ElementViewModel
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    getTasksByLocationUseCase: GetTasksByLocationUseCase,
    getTaskByIdUseCase: GetTaskByIdUseCase,
    markCompleteTaskUseCase: MarkCompleteTaskUseCase,
    getNamesFullLocationElementUseCase: GetNamesFullLocationElementUseCase,
    deleteElementUseCase: DeleteElementUseCase
) : ElementViewModel(
    getTasksByLocationUseCase = getTasksByLocationUseCase,
    getTaskByIdUseCase = getTaskByIdUseCase,
    markCompleteTaskUseCase = markCompleteTaskUseCase,
    getNamesFullLocationElementUseCase = getNamesFullLocationElementUseCase,
    deleteElementUseCase = deleteElementUseCase
) {

    override fun provideScreen(screenState: ScreenState): Screen {
        return screenState.currentScreen as Screen.Task
    }

    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.Task).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }

    /*private val _taskInfo = MutableLiveData<TaskInfo>(TaskInfo.empty())
    val taskInfo: LiveData<TaskInfo> = _taskInfo

    private val _listPedigree = MutableLiveData<List<String>>(emptyList())
    val listPedigree: LiveData<List<String>> = _listPedigree

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
            getListNamesPedigree(
                LocationParam(
                    typeLocation = _taskInfo.value!!.typeLocation,
                    idLocation = _taskInfo.value!!.idLocation ?: 0L
                )
            )

            getListInternalTasks(
                LocationParam(
                    typeLocation = TypeLocation.TASK,
                    idLocation = idTask
                )
            )
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

    private suspend fun getListNamesPedigree(param: LocationParam): Boolean {
        val result = getNamesFullLocationElementUseCase.execute(param)
        when {
            result.isSuccess -> {
                _listPedigree.value = result.getOrDefault(emptyList())
                return true
            }

            result.isFailure -> {
                val e = result.exceptionOrNull()
                Log.d("ttt", "e: $e !!!")
                return false
            }
        }
        return false
    }*/
}

