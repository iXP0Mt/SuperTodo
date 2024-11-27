package com.ixp0mt.supertodo.domain.model

import com.ixp0mt.supertodo.domain.util.TypeLocation

data class FolderInfo(
    val idFolder: Long,
    override val name: String,
    override val description: String?,
    override val typeLocation: TypeLocation,
    override val idLocation: Long,
    val dateCreate: Long,
    val dateEdit: Long? = null,
    val dateArchive: Long? = null
) : ElementInfo {
    companion object {
        fun empty(): FolderInfo {
            return FolderInfo(
                idFolder = 0,
                name = "",
                description = "",
                typeLocation = TypeLocation.MAIN,
                idLocation = 0,
                dateCreate = 0,
                dateEdit = null,
                dateArchive = null
            )
        }

        fun main(): FolderInfo {
            return FolderInfo(
                idFolder = -1,
                name = "Главная папка",
                description = null,
                typeLocation = TypeLocation.MAIN,
                idLocation = 0,
                dateCreate = 0,
                dateEdit = null,
                dateArchive = null
            )
        }
    }
}