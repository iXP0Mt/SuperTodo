package com.ixp0mt.supertodo.domain.model.folder

import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.util.TypeElement

data class FolderInfo(
    override val id: Long = 0,
    override val name: String = "",
    override val description: String? = null,
    override val typeLocation: TypeElement = TypeElement.DEFAULT,
    override val idLocation: Long = 0,
    override val dateCreate: Long = 0,
    override val dateEdit: Long? = null,
    override val dateArchive: Long? = null
) : IElement