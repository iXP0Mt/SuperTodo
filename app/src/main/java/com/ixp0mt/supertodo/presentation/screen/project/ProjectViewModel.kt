package com.ixp0mt.supertodo.presentation.screen.project

import androidx.lifecycle.MutableLiveData
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
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
class ProjectViewModel @Inject constructor(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
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
    private val _projectInfo = MutableLiveData<ProjectInfo>(ProjectInfo.empty())

    override fun getScreen(screenState: ScreenState): Screen? =
        screenState.currentScreen as? Screen.Project

    override fun getIdElementFromArgs(screenState: ScreenState): Long? =
        screenState.currentArgs[Routes.Project.ID]?.toLongOrNull()

    override suspend fun initElement(idElement: Long) {
        initProject(idElement)
    }

    override fun checkAction(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.Project).buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_EDIT -> handleEdit()
                TypeAction.ACTION_DELETE -> handleDelete()
                else -> Unit
            }
        }.launchIn(scope)
    }

    private suspend fun initProject(idProject: Long) {
        if (getProjectInfo(idProject)) {
            val param = LocationParam(TypeLocation.PROJECT, idProject)
            getListInternalFolders(param)
            getListInternalProjects(param)
            getListInternalTasks(param)
        }
    }

    private suspend fun getProjectInfo(idProject: Long): Boolean {
        val param = GetProjectByIdParam(idProject)
        val result = getProjectByIdUseCase.execute(param)
        when {
            result.isSuccess -> {
                _projectInfo.value = result.getOrThrow()
                return true
            }

            result.isFailure -> {
                return false
            }
        }
        return false
    }

    fun deleteCurrentElement() {
        deleteElement(_projectInfo.value!!)
    }
}
