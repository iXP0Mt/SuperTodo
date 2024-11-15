package com.ixp0mt.supertodo.presentation.screen.folder

import androidx.lifecycle.MutableLiveData
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFolderByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.TurnCompleteProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.TurnCompleteTaskUseCase
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
class FolderViewModel @Inject constructor(
    private val getFolderByIdUseCase: GetFolderByIdUseCase,
    getFoldersByLocationUseCase: GetFoldersByLocationUseCase,
    getProjectsByLocationUseCase: GetProjectsByLocationUseCase,
    getTasksByLocationUseCase: GetTasksByLocationUseCase,
    deleteElementUseCase: DeleteElementUseCase,
    turnCompleteTaskUseCase: TurnCompleteTaskUseCase,
    turnCompleteProjectUseCase: TurnCompleteProjectUseCase
) : ElementViewModel(
    getFoldersByLocationUseCase = getFoldersByLocationUseCase,
    getProjectsByLocationUseCase = getProjectsByLocationUseCase,
    getTasksByLocationUseCase = getTasksByLocationUseCase,
    deleteElementUseCase = deleteElementUseCase,
    turnCompleteTaskUseCase = turnCompleteTaskUseCase,
    turnCompleteProjectUseCase = turnCompleteProjectUseCase
) {
    private val _folderInfo = MutableLiveData<FolderInfo>(FolderInfo.empty())


    override fun getScreen(screenState: ScreenState): Screen? =
        screenState.currentScreen as? Screen.Folder

    override fun getIdElementFromArgs(screenState: ScreenState): Long? =
        screenState.currentArgs[Routes.Folder.ID]?.toLongOrNull()

    override suspend fun initElement(idElement: Long) {
        initFolder(idElement)
    }

    override fun checkAction(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.Folder).buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_EDIT -> handleEdit()
                TypeAction.ACTION_DELETE -> handleDelete()
                else -> Unit
            }
        }.launchIn(scope)
    }

    private suspend fun initFolder(idFolder: Long) {
        if (getFolderInfo(idFolder)) {
            val param = LocationParam(TypeLocation.FOLDER, idFolder)
            getListInternalFolders(param)
            getListInternalProjects(param)
            getListInternalTasks(param)
        }
    }

    private suspend fun getFolderInfo(idFolder: Long): Boolean {
        val param = GetFolderByIdParam(idFolder)
        val result = getFolderByIdUseCase.execute(param)
        when {
            result.isSuccess -> {
                _folderInfo.value = result.getOrThrow()
                return true
            }

            result.isFailure -> {
                val e = result.exceptionOrNull()
                return false
            }
        }
        return false
    }

    fun deleteCurrentElement() {
        deleteElement(_folderInfo.value!!)
    }
}
