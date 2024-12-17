package com.ixp0mt.supertodo.presentation.screen.main

import com.ixp0mt.supertodo.domain.usecase.element.GetMainFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetSubElementsWithCountersUseCase
import com.ixp0mt.supertodo.domain.usecase.element.MarkElementCompleteUseCase
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.ElementViewModel
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFolderViewModel @Inject constructor(
    getMainFolderUseCase: GetMainFolderUseCase,
    getSubElementsWithCountersUseCase: GetSubElementsWithCountersUseCase,
    markElementCompleteUseCase: MarkElementCompleteUseCase
) : ElementViewModel(
    getElementUseCase = getMainFolderUseCase,
    getSubElementsWithCountersUseCase = getSubElementsWithCountersUseCase,
    markElementCompleteUseCase = markElementCompleteUseCase
) {
    override suspend fun provideActions(button: TypeAction) {

    }
}