package com.ixp0mt.supertodo.data.local.database.tuple

class ProjectExt(
    val idProject: Long,
    val name: String,
    val dateCreate: Long,
    val dateEdit: Long?,
    val dateCompleted: Long?,
    val countSubFolders: Int,
    val countSubProjects: Int,
    val countSubTasks: Int,
)