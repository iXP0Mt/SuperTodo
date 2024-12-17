package com.ixp0mt.supertodo.domain.model

import com.ixp0mt.supertodo.domain.util.TypeElement

class MarkElementCompleteParam(
    val typeElement: TypeElement,
    val idElement: Long,
    val isComplete: Boolean
)