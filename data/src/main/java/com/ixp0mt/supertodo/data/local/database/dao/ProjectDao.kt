package com.ixp0mt.supertodo.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ixp0mt.supertodo.data.local.database.entity.Project
import com.ixp0mt.supertodo.data.local.database.tuple.ProjectExt
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

    @Query("""
        SELECT 
            projects.idProject,
            projects.name,
            projects.dateCreate,
            projects.dateEdit,
            projects.dateCompleted,
            COUNT(DISTINCT subFolders.idFolder) AS countSubFolders,
            COUNT(DISTINCT subProjects.idProject) AS countSubProjects,
            COUNT(DISTINCT subTasks.idTask) AS countSubTasks
        FROM projects
        LEFT JOIN folders subFolders ON subFolders.idLocation = projects.idProject AND subFolders.typeLocation = :elementLocation
        LEFT JOIN projects subProjects ON subProjects.idLocation = projects.idProject AND subProjects.typeLocation = :elementLocation
        LEFT JOIN tasks subTasks ON subTasks.idLocation = projects.idProject AND subTasks.typeLocation = :elementLocation
        WHERE projects.idLocation = :idLocation AND projects.typeLocation = :typeLocation
        GROUP BY projects.idProject
    """)
    suspend fun getWithCountsInternalElements(typeLocation: TypeLocation, idLocation: Long, elementLocation: TypeLocation = TypeLocation.PROJECT): List<ProjectExt>
}