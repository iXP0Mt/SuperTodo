package com.ixp0mt.supertodo.presentation.screen.task

import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetNamesFullLocationElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersWithCountsSubElementsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
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
class TaskViewModel @Inject constructor(
    //getTasksByLocationUseCase: GetTasksByLocationUseCase,
    getFoldersWithCountsSubElementsByLocationUseCase: GetFoldersWithCountsSubElementsByLocationUseCase,
    getTaskByIdUseCase: GetTaskByIdUseCase,
    markCompleteTaskUseCase: MarkCompleteTaskUseCase,
    getNamesFullLocationElementUseCase: GetNamesFullLocationElementUseCase,
    deleteElementUseCase: DeleteElementUseCase
) : ElementViewModel(
    //getTasksByLocationUseCase = getTasksByLocationUseCase,
    getFoldersWithCountsSubElementsByLocationUseCase = getFoldersWithCountsSubElementsByLocationUseCase,
    getTaskByIdUseCase = getTaskByIdUseCase,
    markCompleteTaskUseCase = markCompleteTaskUseCase,
    getNamesFullLocationElementUseCase = getNamesFullLocationElementUseCase,
    deleteElementUseCase = deleteElementUseCase
) {
    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.Task).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }
}

