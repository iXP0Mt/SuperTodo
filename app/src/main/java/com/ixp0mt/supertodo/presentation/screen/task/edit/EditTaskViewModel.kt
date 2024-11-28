package com.ixp0mt.supertodo.presentation.screen.task.edit

import com.ixp0mt.supertodo.domain.usecase.element.GetElementByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.task.SaveEditTaskUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.EditElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    getTaskByIdUseCase: GetTaskByIdUseCase,
    saveEditTaskUseCase: SaveEditTaskUseCase,
    getElementByLocationUseCase: GetElementByLocationUseCase
) : EditElementViewModel(
    getTaskByIdUseCase = getTaskByIdUseCase,
    saveEditTaskUseCase = saveEditTaskUseCase,
    getElementByLocationUseCase = getElementByLocationUseCase
) {
    override fun provideScreen(screenState: ScreenState): Screen {
        return screenState.currentScreen as Screen.TaskEdit
    }

    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.TaskEdit).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }
}

