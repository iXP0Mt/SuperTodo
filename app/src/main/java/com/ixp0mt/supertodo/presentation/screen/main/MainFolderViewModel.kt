package com.ixp0mt.supertodo.presentation.screen.main

import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.MarkCompleteProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.MarkCompleteTaskUseCase
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.BaseViewModel
import com.ixp0mt.supertodo.presentation.screen.core.ElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFolderViewModel @Inject constructor(
    getFoldersByLocationUseCase: GetFoldersByLocationUseCase,
    getProjectsByLocationUseCase: GetProjectsByLocationUseCase,
    getTasksByLocationUseCase: GetTasksByLocationUseCase,
    markCompleteProjectUseCase: MarkCompleteProjectUseCase,
    markCompleteTaskUseCase: MarkCompleteTaskUseCase,
) : ElementViewModel(
    getFoldersByLocationUseCase = getFoldersByLocationUseCase,
    getProjectsByLocationUseCase = getProjectsByLocationUseCase,
    getTasksByLocationUseCase = getTasksByLocationUseCase,
    markCompleteProjectUseCase = markCompleteProjectUseCase,
    markCompleteTaskUseCase = markCompleteTaskUseCase
) {
    /*override fun getScreen(screenState: ScreenState): Screen =
         screenState.currentScreen as Screen.MainFolder

    override fun getIdElementFromArgs(screenState: ScreenState): Long = 0

    override suspend fun initElement(idElement: Long) {
        val param = LocationParam(typeLocation = TypeLocation.MAIN, idLocation = 0)
        getListInternalFolders(param)
        getListInternalProjects(param)
        getListInternalTasks(param)
    }*/
    override fun provideScreen(screenState: ScreenState): Screen {
        return screenState.currentScreen as Screen.MainFolder
    }

    override fun provideActions(screen: Screen, scope: CoroutineScope) {}

    override fun initElement(screenState: ScreenState) {
        viewModelScope.launch {
            loadInternalElements(TypeElement.MAIN, 0)
        }
    }
}