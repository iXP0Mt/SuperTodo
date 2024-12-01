package com.ixp0mt.supertodo.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ixp0mt.supertodo.domain.util.TypeLocation

@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey(autoGenerate = true) val idFolder: Long = 0,
    val name: String,
    val description: String?,
    val typeLocation: TypeLocation,
    val idLocation: Long?,
    val dateCreate: Long,
    val dateEdit: Long?,
    val dateArchive: Long?
)

