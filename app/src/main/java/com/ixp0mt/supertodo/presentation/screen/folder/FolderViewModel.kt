package com.ixp0mt.supertodo.presentation.screen.folder

import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetListNamesPedigreeUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetSubElementsWithCountersUseCase
import com.ixp0mt.supertodo.domain.usecase.element.MarkElementCompleteUseCase
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EssentialElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    getListNamesPedigreeUseCase: GetListNamesPedigreeUseCase,
    deleteElementUseCase: DeleteElementUseCase,
    getFolderUseCase: GetFolderUseCase,
    getSubElementsWithCountersUseCase: GetSubElementsWithCountersUseCase,
    markElementCompleteUseCase: MarkElementCompleteUseCase
) : EssentialElementViewModel(
    getListNamesPedigreeUseCase = getListNamesPedigreeUseCase,
    deleteElementUseCase = deleteElementUseCase,
    getElementUseCase = getFolderUseCase,
    getSubElementsWithCountersUseCase = getSubElementsWithCountersUseCase,
    markElementCompleteUseCase = markElementCompleteUseCase
)