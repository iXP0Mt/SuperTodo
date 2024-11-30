package com.ixp0mt.supertodo.presentation.screen.project.create

import com.ixp0mt.supertodo.domain.usecase.element.GetElementByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.SaveNewProjectUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.CreateElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    saveNewProjectUseCase: SaveNewProjectUseCase,
    getElementByLocationUseCase: GetElementByLocationUseCase
) : CreateElementViewModel(
    saveNewProjectUseCase = saveNewProjectUseCase,
    getElementByLocationUseCase = getElementByLocationUseCase
) {
    override fun provideScreen(screenState: ScreenState): Screen {
        return screenState.currentScreen as Screen.ProjectCreate
    }

    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.ProjectCreate).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }
}
