package com.ixp0mt.supertodo.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ixp0mt.supertodo.domain.util.TypeElement

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val idTask: Long = 0,
    val name: String,
    val description: String?,
    val typeLocation: TypeElement,
    val idLocation: Long?,
    val dateCreate: Long,
    val dateEdit: Long?,
    val dateArchive: Long?,
    val dateStart: Long?,
    val dateEnd: Long?,
    val dateCompleted: Long?
)
