package com.ixp0mt.supertodo.presentation.screen.task.create

import com.ixp0mt.supertodo.domain.usecase.element.GetElementByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.SaveNewTaskUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.CreateElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    saveNewTaskUseCase: SaveNewTaskUseCase,
    getElementByLocationUseCase: GetElementByLocationUseCase
) : CreateElementViewModel(
    saveNewTaskUseCase = saveNewTaskUseCase,
    getElementByLocationUseCase = getElementByLocationUseCase
) {
    override fun provideScreen(screenState: ScreenState): Screen {
        return screenState.currentScreen as Screen.TaskCreate
    }

    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.TaskCreate).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }
}

