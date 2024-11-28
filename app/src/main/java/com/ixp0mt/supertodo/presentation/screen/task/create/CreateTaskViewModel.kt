package com.ixp0mt.supertodo.presentation.screen.task.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.task.SaveNewTaskUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.CreateElementViewModel
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    saveNewTaskUseCase: SaveNewTaskUseCase
) : CreateElementViewModel(
    saveNewTaskUseCase = saveNewTaskUseCase
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

