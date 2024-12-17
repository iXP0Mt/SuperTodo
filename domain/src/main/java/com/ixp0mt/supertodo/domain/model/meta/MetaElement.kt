package com.ixp0mt.supertodo.domain.model.meta

import com.ixp0mt.supertodo.domain.model.element.IElementMeta

sealed class MetaElement(
    override val id: Long,
    override val name: String,
    override val description: String?
) : IElementMeta {
    data object MainFolder : MetaElement(0, "Главная папка", null)
    data object Default : MetaElement(0, "NULL", null)
}