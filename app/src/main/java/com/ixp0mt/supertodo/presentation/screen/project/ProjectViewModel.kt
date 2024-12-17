package com.ixp0mt.supertodo.presentation.screen.project

import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetListNamesPedigreeUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetSubElementsWithCountersUseCase
import com.ixp0mt.supertodo.domain.usecase.element.MarkElementCompleteUseCase
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EssentialElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    getListNamesPedigreeUseCase: GetListNamesPedigreeUseCase,
    deleteElementUseCase: DeleteElementUseCase,
    getProjectUseCase: GetProjectUseCase,
    getSubElementsWithCountersUseCase: GetSubElementsWithCountersUseCase,
    markElementCompleteUseCase: MarkElementCompleteUseCase
) : EssentialElementViewModel(
    getListNamesPedigreeUseCase = getListNamesPedigreeUseCase,
    deleteElementUseCase = deleteElementUseCase,
    getElementUseCase = getProjectUseCase,
    getSubElementsWithCountersUseCase = getSubElementsWithCountersUseCase,
    markElementCompleteUseCase = markElementCompleteUseCase
)
