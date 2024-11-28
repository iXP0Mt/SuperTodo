package com.ixp0mt.supertodo.presentation.screen.folder.edit

import com.ixp0mt.supertodo.domain.usecase.element.GetElementByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFolderByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.SaveEditFolderUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.EditElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class EditFolderViewModel @Inject constructor(
    getFolderByIdUseCase: GetFolderByIdUseCase,
    saveEditFolderUseCase: SaveEditFolderUseCase,
    getElementByLocationUseCase: GetElementByLocationUseCase
) : EditElementViewModel(
    getFolderByIdUseCase = getFolderByIdUseCase,
    saveEditFolderUseCase = saveEditFolderUseCase,
    getElementByLocationUseCase = getElementByLocationUseCase
) {
    override fun provideScreen(screenState: ScreenState): Screen {
        return screenState.currentScreen as Screen.FolderEdit
    }

    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.FolderEdit).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }
}