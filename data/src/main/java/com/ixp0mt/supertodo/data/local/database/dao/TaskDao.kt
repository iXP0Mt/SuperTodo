package com.ixp0mt.supertodo.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ixp0mt.supertodo.data.local.database.entity.Task
import com.ixp0mt.supertodo.data.local.database.tuple.TaskExt
import com.ixp0mt.supertodo.domain.util.TypeElement

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task): Int

    @Delete
    suspend fun delete(task: Task): Int

    @Query("SELECT * FROM tasks WHERE typeLocation = :typeLocation AND idLocation = :idLocation")
    suspend fun getByLocation(typeLocation: TypeElement, idLocation: Long): List<Task>

    @Query("SELECT * FROM tasks WHERE idTask = :idTask")
    suspend fun getById(idTask: Long): Task

    @Query("DELETE FROM tasks WHERE typeLocation = :typeLocation AND idLocation = :idLocation")
    suspend fun deleteByLocation(typeLocation: TypeElement, idLocation: Long): Int

    @Query("UPDATE tasks SET dateCompleted = :date WHERE idTask = :idTask")
    suspend fun setComplete(idTask: Long, date: Long): Int

    @Query("UPDATE tasks SET dateCompleted = null WHERE idTask = :idTask")
    suspend fun removeComplete(idTask: Long): Int

    @Query("""
        SELECT 
            tasks.idTask,
            tasks.name,
            tasks.dateCreate,
            tasks.dateEdit,
            tasks.dateCompleted,
            COUNT(DISTINCT subFolders.idFolder) AS countSubFolders,
            COUNT(DISTINCT subProjects.idProject) AS countSubProjects,
            COUNT(DISTINCT subTasks.idTask) AS countSubTasks
        FROM tasks
        LEFT JOIN folders subFolders ON subFolders.idLocation = tasks.idTask AND subFolders.typeLocation = :elementLocation
        LEFT JOIN projects subProjects ON subProjects.idLocation = tasks.idTask AND subProjects.typeLocation = :elementLocation AND subProjects.dateCompleted IS NULL
        LEFT JOIN tasks subTasks ON subTasks.idLocation = tasks.idTask AND subTasks.typeLocation = :elementLocation AND subTasks.dateCompleted IS NULL
        WHERE tasks.idLocation = :idLocation AND tasks.typeLocation = :typeLocation
        GROUP BY tasks.idTask
    """)
    suspend fun getWithCountsInternalElements(typeLocation: TypeElement, idLocation: Long, elementLocation: TypeElement = TypeElement.TASK): List<TaskExt>
}