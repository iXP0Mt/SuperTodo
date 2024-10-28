package com.ixp0mt.supertodo.presentation.screen.main

import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.TurnCompleteTaskUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.ElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFolderViewModel @Inject constructor(
    getFoldersByLocationUseCase: GetFoldersByLocationUseCase,
    getProjectsByLocationUseCase: GetProjectsByLocationUseCase,
    getTasksByLocationUseCase: GetTasksByLocationUseCase,
    deleteElementUseCase: DeleteElementUseCase,
    turnCompleteTaskUseCase: TurnCompleteTaskUseCase
) : ElementViewModel(
    getFoldersByLocationUseCase = getFoldersByLocationUseCase,
    getProjectsByLocationUseCase = getProjectsByLocationUseCase,
    getTasksByLocationUseCase = getTasksByLocationUseCase,
    deleteElementUseCase = deleteElementUseCase,
    turnCompleteTaskUseCase = turnCompleteTaskUseCase
) {
    override fun getScreen(screenState: ScreenState): Screen =
         screenState.currentScreen as Screen.MainFolder

    override fun getIdElementFromArgs(screenState: ScreenState): Long = 0

    override suspend fun initElement(idElement: Long) {
        val param = LocationParam(typeLocation = TypeLocation.MAIN, idLocation = 0)
        getListInternalFolders(param)
        getListInternalProjects(param)
        getListInternalTasks(param)
    }
}