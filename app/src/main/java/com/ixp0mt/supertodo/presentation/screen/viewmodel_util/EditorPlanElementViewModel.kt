package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.lifecycle.MutableLiveData
import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.usecase.element.CreateElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetLocationAsElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.ValidElementUseCase

open class EditorPlanElementViewModel(

    getElementUseCase: GetElementUseCase,
    validElementUseCase: ValidElementUseCase,
    getLocationAsElementUseCase: GetLocationAsElementUseCase,
    private val createElementUseCase: CreateElementUseCase,
    saveElementUseCase: SaveElementUseCase

) : EditorElementViewModel(

    getElementUseCase = getElementUseCase,
    validElementUseCase = validElementUseCase,
    getLocationAsElementUseCase = getLocationAsElementUseCase,
    createElementUseCase = createElementUseCase,
    saveElementUseCase = saveElementUseCase
) {

    private val _datePlanStart = MutableLiveData<Long?>(null)
    private val _datePlanEnd = MutableLiveData<Long?>(null)
    private val _dateFactEnd = MutableLiveData<Long?>(null)

    override fun prepareElementToSave(): IElement? {
        val element = super.prepareElementToSave() ?: return null

        return createElementUseCase(
            element.id,
            element.name,
            element.description,
            element.typeLocation,
            element.idLocation,
            element.dateCreate,
            element.dateEdit,
            element.dateArchive,
            _datePlanStart.value,
            _datePlanEnd.value,
            _dateFactEnd.value
        )
    }
}