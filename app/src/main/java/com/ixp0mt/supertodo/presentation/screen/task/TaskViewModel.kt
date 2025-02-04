package com.ixp0mt.supertodo.presentation.screen.task

import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetListNamesPedigreeUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetSubElementsWithCountersUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.element.MarkElementCompleteUseCase
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EssentialElementViewModel
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EssentialPlanElementViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    getListNamesPedigreeUseCase: GetListNamesPedigreeUseCase,
    deleteElementUseCase: DeleteElementUseCase,
    getTaskUseCase: GetTaskUseCase,
    getSubElementsWithCountersUseCase: GetSubElementsWithCountersUseCase,
    markElementCompleteUseCase: MarkElementCompleteUseCase
) : EssentialPlanElementViewModel(
    getListNamesPedigreeUseCase = getListNamesPedigreeUseCase,
    deleteElementUseCase = deleteElementUseCase,
    getElementUseCase = getTaskUseCase,
    getSubElementsWithCountersUseCase = getSubElementsWithCountersUseCase,
    markElementCompleteUseCase = markElementCompleteUseCase
)