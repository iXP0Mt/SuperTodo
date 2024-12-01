package com.ixp0mt.supertodo.presentation.screen.main

import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersWithCountsSubElementsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.MarkCompleteProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.MarkCompleteTaskUseCase
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.ElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFolderViewModel @Inject constructor(
    //getFoldersByLocationUseCase: GetFoldersByLocationUseCase,
    getFoldersWithCountsSubElementsByLocationUseCase: GetFoldersWithCountsSubElementsByLocationUseCase,
    getProjectsByLocationUseCase: GetProjectsByLocationUseCase,
    getTasksByLocationUseCase: GetTasksByLocationUseCase,
    markCompleteProjectUseCase: MarkCompleteProjectUseCase,
    markCompleteTaskUseCase: MarkCompleteTaskUseCase,
) : ElementViewModel(
    //getFoldersByLocationUseCase = getFoldersByLocationUseCase,
    getFoldersWithCountsSubElementsByLocationUseCase = getFoldersWithCountsSubElementsByLocationUseCase,
    getProjectsByLocationUseCase = getProjectsByLocationUseCase,
    getTasksByLocationUseCase = getTasksByLocationUseCase,
    markCompleteProjectUseCase = markCompleteProjectUseCase,
    markCompleteTaskUseCase = markCompleteTaskUseCase
) {
    override fun provideActions(screen: Screen, scope: CoroutineScope) {}

    override fun initElement(screenState: ScreenState) {
        viewModelScope.launch {
            loadInternalElementsByLocation(TypeLocation.MAIN, 0, 0b011)
            loadSubElementsWithCountsSubElementsByLocation(TypeLocation.MAIN, 0)
            setFormattedCountersForSubElements()
        }
    }
}