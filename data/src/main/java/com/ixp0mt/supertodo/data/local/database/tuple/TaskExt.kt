package com.ixp0mt.supertodo.data.local.database.tuple

class TaskExt(
    val idTask: Long,
    val name: String,
    val dateCreate: Long,
    val dateEdit: Long?,
    val dateCompleted: Long?,
    val countSubTasks: Int,
)