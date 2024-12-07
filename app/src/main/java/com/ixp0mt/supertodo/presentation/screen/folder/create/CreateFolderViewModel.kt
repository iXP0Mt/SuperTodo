package com.ixp0mt.supertodo.presentation.screen.folder.create

import com.ixp0mt.supertodo.domain.usecase.element.GetElementByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.SaveNewFolderUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.screen.core.CreateElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreateFolderViewModel @Inject constructor(
    saveNewFolderUseCase: SaveNewFolderUseCase,
    getElementByLocationUseCase: GetElementByLocationUseCase
) : CreateElementViewModel(
    saveNewFolderUseCase = saveNewFolderUseCase,
    getElementByLocationUseCase = getElementByLocationUseCase
) {
    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.FolderCreate).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }
}
