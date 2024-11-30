package com.ixp0mt.supertodo.presentation.screen.project

import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetNamesFullLocationElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.MarkCompleteProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.MarkCompleteTaskUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.screen.core.ElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    getFoldersByLocationUseCase: GetFoldersByLocationUseCase,
    getProjectsByLocationUseCase: GetProjectsByLocationUseCase,
    getTasksByLocationUseCase: GetTasksByLocationUseCase,
    getProjectByIdUseCase: GetProjectByIdUseCase,
    markCompleteProjectUseCase: MarkCompleteProjectUseCase,
    markCompleteTaskUseCase: MarkCompleteTaskUseCase,
    getNamesFullLocationElementUseCase: GetNamesFullLocationElementUseCase,
    deleteElementUseCase: DeleteElementUseCase
) : ElementViewModel(
    getFoldersByLocationUseCase = getFoldersByLocationUseCase,
    getProjectsByLocationUseCase = getProjectsByLocationUseCase,
    getTasksByLocationUseCase = getTasksByLocationUseCase,
    getProjectByIdUseCase = getProjectByIdUseCase,
    markCompleteProjectUseCase = markCompleteProjectUseCase,
    markCompleteTaskUseCase = markCompleteTaskUseCase,
    getNamesFullLocationElementUseCase = getNamesFullLocationElementUseCase,
    deleteElementUseCase = deleteElementUseCase
) {
    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.Project).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }
}
