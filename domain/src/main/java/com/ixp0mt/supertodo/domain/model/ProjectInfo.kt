package com.ixp0mt.supertodo.domain.model

import com.ixp0mt.supertodo.domain.util.TypeLocation

data class ProjectInfo(
    val idProject: Long,
    override val name: String,
    val description: String?,
    override val typeLocation: TypeLocation,
    override val idLocation: Long?,
    val dateCreate: Long,
    val dateEdit: Long?,
    val dateArchive: Long?,
    val dateStart: Long?,
    val dateEnd: Long?,
    val dateCompleted: Long?
) : ElementInfo {
    companion object {
        fun empty() = ProjectInfo(
            idProject = 0,
            name = "",
            description = "",
            typeLocation = TypeLocation.MAIN,
            idLocation = null,
            dateCreate = 0,
            dateEdit = null,
            dateArchive = null,
            dateStart = null,
            dateEnd = null,
            dateCompleted = null
        )
    }
}