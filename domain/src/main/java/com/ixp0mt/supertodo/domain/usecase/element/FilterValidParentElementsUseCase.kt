package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.util.TypeElement

class FilterValidParentElementsUseCase {
    operator fun invoke(typeElement: TypeElement, list: List<IElement>): List<IElement> {

        if(typeElement == TypeElement.TASK) return list

        val correctTypesElse = listOf(TypeElement.FOLDER, TypeElement.PROJECT)

        val getTypeElementUseCase = GetTypeElementUseCase()
        return list.filter {
            getTypeElementUseCase(it) in correctTypesElse
        }
    }
}