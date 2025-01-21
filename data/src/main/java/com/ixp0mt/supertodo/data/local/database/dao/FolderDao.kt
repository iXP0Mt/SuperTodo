package com.ixp0mt.supertodo.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ixp0mt.supertodo.data.local.database.entity.Folder
import com.ixp0mt.supertodo.data.local.database.tuple.FolderExt
import com.ixp0mt.supertodo.domain.util.TypeElement

@Dao
interface FolderDao {
    @Insert
    suspend fun insert(folder: Folder): Long

    @Update
    suspend fun update(folder: Folder): Int

    @Delete
    suspend fun delete(folder: Folder): Int

    @Query("SELECT * FROM folders WHERE idFolder = :idFolder")
    suspend fun getById(idFolder: Long): Folder

    @Query("SELECT * FROM folders WHERE typeLocation = :typeLocation AND idLocation = :idLocation")
    suspend fun getByLocation(typeLocation: TypeElement, idLocation: Long): List<Folder>

    @Query("DELETE FROM folders WHERE typeLocation = :typeLocation AND idLocation = :idLocation")
    suspend fun deleteByLocation(typeLocation: TypeElement, idLocation: Long): Int

    @Query("""
        SELECT 
            folders.idFolder,
            folders.name,
            folders.dateCreate,
            folders.dateEdit,
            COUNT(DISTINCT subFolders.idFolder) AS countSubFolders,
            COUNT(DISTINCT subProjects.idProject) AS countSubProjects,
            COUNT(DISTINCT subTasks.idTask) AS countSubTasks
        FROM folders
        LEFT JOIN folders subFolders ON subFolders.idLocation = folders.idFolder AND subFolders.typeLocation = :elementLocation
        LEFT JOIN projects subProjects ON subProjects.idLocation = folders.idFolder AND subProjects.typeLocation = :elementLocation AND subProjects.dateCompleted IS NULL
        LEFT JOIN tasks subTasks ON subTasks.idLocation = folders.idFolder AND subTasks.typeLocation = :elementLocation AND subTasks.dateCompleted IS NULL
        WHERE folders.idLocation = :idLocation AND folders.typeLocation = :typeLocation
        GROUP BY folders.idFolder
    """)
    suspend fun getWithCountsInternalElements(typeLocation: TypeElement, idLocation: Long, elementLocation: TypeElement = TypeElement.FOLDER): List<FolderExt>
}