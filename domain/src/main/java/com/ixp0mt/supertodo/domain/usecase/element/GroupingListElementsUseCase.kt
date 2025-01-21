package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.element.IElement

class GroupingListElementsUseCase {
    operator fun invoke(listElements: List<IElement>): List<IElement> {
        return listElements.groupOnDateEnd()
    }

    private fun List<IElement>.groupOnDateEnd(): List<IElement> {
        val isElementCompleteUseCase = IsElementCompleteUseCase()
        return this.sortedBy { isElementCompleteUseCase(it) }
    }
}