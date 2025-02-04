package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ixp0mt.supertodo.domain.model.element.IElementPlan
import com.ixp0mt.supertodo.domain.usecase.datetime.FormatDateTimeUseCase
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetListNamesPedigreeUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetSubElementsWithCountersUseCase
import com.ixp0mt.supertodo.domain.usecase.element.MarkElementCompleteUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState

open class EssentialPlanElementViewModel(
    getListNamesPedigreeUseCase: GetListNamesPedigreeUseCase,
    deleteElementUseCase: DeleteElementUseCase,
    getElementUseCase: GetElementUseCase,
    getSubElementsWithCountersUseCase: GetSubElementsWithCountersUseCase,
    markElementCompleteUseCase: MarkElementCompleteUseCase
) : EssentialElementViewModel(
    getListNamesPedigreeUseCase = getListNamesPedigreeUseCase,
    deleteElementUseCase = deleteElementUseCase,
    getElementUseCase = getElementUseCase,
    getSubElementsWithCountersUseCase = getSubElementsWithCountersUseCase,
    markElementCompleteUseCase = markElementCompleteUseCase
) {
    private val _labelStartDate = MutableLiveData<String?>(null)
    val labelStartDate: LiveData<String?> = _labelStartDate

    private val _labelStartTime = MutableLiveData<String?>(null)
    val labelStartTime: LiveData<String?> = _labelStartTime

    private val _labelEndDate = MutableLiveData<String?>(null)
    val labelEndDate: LiveData<String?> = _labelEndDate

    private val _labelEndTime = MutableLiveData<String?>(null)
    val labelEndTime: LiveData<String?> = _labelEndTime


    override suspend fun initData(screenState: ScreenState) {
        super.initData(screenState)

        val formatter = FormatDateTimeUseCase()

        val currentElement = _currentElement.value as IElementPlan
        val dateStart = currentElement.datePlanStart
        val dateEnd = currentElement.datePlanEnd

        _labelStartDate.value = formatter.formatDate(dateStart)
        _labelStartTime.value = formatter.formatTime(dateStart)
        _labelEndDate.value = formatter.formatDate(dateEnd)
        _labelEndTime.value = formatter.formatTime(dateEnd)
    }
}