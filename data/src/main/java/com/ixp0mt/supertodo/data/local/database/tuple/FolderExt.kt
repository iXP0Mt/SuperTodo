package com.ixp0mt.supertodo.data.local.database.tuple

data class FolderExt(
    val idFolder: Long,
    val name: String,
    val dateCreate: Long,
    val dateEdit: Long?,
    val countSubFolders: Int,
    val countSubProjects: Int,
    val countSubTasks: Int
)