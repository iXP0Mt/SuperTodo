package com.ixp0mt.supertodo.presentation.util

import androidx.annotation.DrawableRes
import com.ixp0mt.supertodo.domain.util.TypeElement

data class ElementCardInfo(
    val id: Long,
    val typeElement: TypeElement,
    @DrawableRes val iconId: Int,
    val name: String,
    val isComplete: Boolean,
    val strCounters: String?
)