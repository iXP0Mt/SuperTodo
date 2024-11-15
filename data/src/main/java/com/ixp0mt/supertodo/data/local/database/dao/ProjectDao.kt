package com.ixp0mt.supertodo.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ixp0mt.supertodo.data.local.database.entity.Project
import com.ixp0mt.supertodo.domain.util.TypeLocation

@Dao
interface ProjectDao {
    @Insert
    suspend fun insert(project: Project): Long

    @Update
    suspend fun update(project: Project): Int

    @Delete
    suspend fun delete(project: Project): Int

    @Query("SELECT * FROM projects WHERE typeLocation = :typeLocation")
    suspend fun getByTypeLocation(typeLocation: TypeLocation): List<Project>

    @Query("SELECT * FROM projects WHERE typeLocation = :typeLocation AND idLocation = :idLocation")
    suspend fun getByLocation(typeLocation: TypeLocation, idLocation: Long): List<Project>

    @Query("SELECT * FROM projects WHERE idProject = :idProject")
    suspend fun getById(idProject: Long): Project

    @Query("DELETE FROM projects WHERE typeLocation = :typeLocation AND idLocation = :idLocation")
    suspend fun deleteByLocation(typeLocation: TypeLocation, idLocation: Long): Int

    @Query("UPDATE projects SET dateCompleted = :date WHERE idProject = :idProject")
    suspend fun setComplete(idProject: Long, date: Long): Int

    @Query("UPDATE projects SET dateCompleted = null WHERE idProject = :idProject")
    suspend fun removeComplete(idProject: Long): Int
}