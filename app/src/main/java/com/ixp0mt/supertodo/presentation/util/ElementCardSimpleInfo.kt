package com.ixp0mt.supertodo.presentation.util

import androidx.annotation.DrawableRes
import com.ixp0mt.supertodo.domain.util.TypeElement

data class ElementCardSimpleInfo(
    val idElement: Long,
    val typeElement: TypeElement,
    @DrawableRes val iconId: Int,
    val name: String,
    val block: Boolean
)