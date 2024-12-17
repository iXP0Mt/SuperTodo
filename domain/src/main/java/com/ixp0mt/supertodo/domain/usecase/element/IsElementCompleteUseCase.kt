package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.model.element.IElementPlan

class IsElementCompleteUseCase {
    operator fun invoke(element: IElement): Boolean {
        return element is IElementPlan && element.dateFactEnd != null
    }
}