package com.ixp0mt.supertodo.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ixp0mt.supertodo.data.local.database.entity.Task
import com.ixp0mt.supertodo.domain.util.TypeLocation

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task): Int

    @Delete
    suspend fun delete(task: Task): Int

    @Query("SELECT * FROM tasks WHERE typeLocation = :typeLocation")
    suspend fun getByTypeLocation(typeLocation: TypeLocation): List<Task>

    @Query("SELECT * FROM tasks WHERE typeLocation = :typeLocation AND idLocation = :idLocation")
    suspend fun getByLocation(typeLocation: TypeLocation, idLocation: Long): List<Task>

    @Query("SELECT * FROM tasks WHERE idTask = :idTask")
    suspend fun getById(idTask: Long): Task

    @Query("DELETE FROM tasks WHERE typeLocation = :typeLocation AND idLocation = :idLocation")
    suspend fun deleteByLocation(typeLocation: TypeLocation, idLocation: Long): Int

    @Query("UPDATE tasks SET dateCompleted = :date WHERE idTask = :idTask")
    suspend fun setComplete(idTask: Long, date: Long): Int

    @Query("UPDATE tasks SET dateCompleted = null WHERE idTask = :idTask")
    suspend fun removeComplete(idTask: Long): Int
}