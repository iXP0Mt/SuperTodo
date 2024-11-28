package com.ixp0mt.supertodo.presentation.screen.folder.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.usecase.folder.SaveNewFolderUseCase
import com.ixp0mt.supertodo.presentation.util.TypeAction
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.CreateElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFolderViewModel @Inject constructor(
    saveNewFolderUseCase: SaveNewFolderUseCase
) : CreateElementViewModel(
    saveNewFolderUseCase = saveNewFolderUseCase
) {
    override fun provideScreen(screenState: ScreenState): Screen {
        return screenState.currentScreen as Screen.FolderCreate
    }

    override fun provideActions(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.FolderCreate).buttons.onEach { button ->
            handleAction(button)
        }.launchIn(scope)
    }
}
