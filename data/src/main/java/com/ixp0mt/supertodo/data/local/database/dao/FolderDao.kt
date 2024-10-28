package com.ixp0mt.supertodo.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ixp0mt.supertodo.data.local.database.entity.Folder
import com.ixp0mt.supertodo.domain.util.TypeLocation

@Dao
interface FolderDao {
    @Insert
    suspend fun insert(folder: Folder): Long

    @Update
    suspend fun update(folder: Folder): Int

    @Delete
    suspend fun delete(folder: Folder): Int

    @Query("SELECT * FROM folders WHERE typeLocation = :typeLocation")
    suspend fun getByTypeLocation(typeLocation: TypeLocation): List<Folder>

    @Query("SELECT * FROM folders WHERE idFolder = :idFolder")
    suspend fun getById(idFolder: Long): Folder

    @Query("SELECT * FROM folders WHERE typeLocation = :typeLocation AND idLocation = :idLocation")
    suspend fun getByLocation(typeLocation: TypeLocation, idLocation: Long): List<Folder>

    @Query("DELETE FROM folders WHERE typeLocation = :typeLocation AND idLocation = :idLocation")
    suspend fun deleteByLocation(typeLocation: TypeLocation, idLocation: Long): Int
}