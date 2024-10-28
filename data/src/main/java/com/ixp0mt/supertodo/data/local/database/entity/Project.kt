package com.ixp0mt.supertodo.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ixp0mt.supertodo.domain.util.TypeLocation

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val idProject: Long = 0,
    val name: String,
    val description: String?,
    val typeLocation: TypeLocation,
    val idLocation: Long?,
    val dateCreate: Long,
    val dateEdit: Long?,
    val dateArchive: Long?,
    val dateStart: Long?,
    val dateEnd: Long?,
    val dateCompleted: Long?
)
