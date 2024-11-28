package com.ixp0mt.supertodo.presentation.screen.project.edit

import com.ixp0mt.supertodo.domain.usecase.element.GetElementByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.project.SaveEditProjectUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.EditElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class EditProjectViewModel @Inject constructor(
    getProjectByIdUseCase: GetProjectByIdUseCase,
    saveEditProjectUseCase: SaveEditProjectUseCase,
    getElementByLocationUseCase: GetElementByLocationUseCase
) : EditElementViewModel(
    getProjectByIdUseCase = getProjectByIdUseCase,
    saveEditProjectUseCase = saveEditProjectUseCase,
    getElementByLocationUseCase = getElementByLocationUseCase
) {
    override fun provideScreen(screenState: ScreenState): Screen {
        return screenState.currentScreen as Screen.ProjectEdit
    }

    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.ProjectEdit).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }
}

