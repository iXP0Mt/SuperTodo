package com.ixp0mt.supertodo.domain.model

import com.ixp0mt.supertodo.domain.util.TypeLocation

interface ElementInfo {
    val name: String
    val description: String?
    val typeLocation: TypeLocation
    val idLocation: Long
}