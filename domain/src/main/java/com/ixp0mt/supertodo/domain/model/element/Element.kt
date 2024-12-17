package com.ixp0mt.supertodo.domain.model.element

import com.ixp0mt.supertodo.domain.util.TypeElement

interface IElementMeta {
    val id: Long
    val name: String
    val description: String?
}


interface IElement : IElementMeta {
    val typeLocation: TypeElement
    val idLocation: Long
    val dateCreate: Long
    val dateEdit: Long?
    val dateArchive: Long?
}


interface IElementPlan : IElement {
    val datePlanStart: Long?
    val datePlanEnd: Long?
    val dateFactEnd: Long?
}