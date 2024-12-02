package com.ixp0mt.supertodo.data.local.database

import android.content.Context
import androidx.room.Room
import com.ixp0mt.supertodo.data.local.database.entity.Folder
import com.ixp0mt.supertodo.data.local.database.entity.Project
import com.ixp0mt.supertodo.data.local.database.entity.Task
import com.ixp0mt.supertodo.data.local.database.tuple.FolderExt
import com.ixp0mt.supertodo.data.local.database.tuple.ProjectExt
import com.ixp0mt.supertodo.data.local.database.tuple.TaskExt
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.model.GetFoldersByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.GetProjectsByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.GetTasksByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam

class DatabaseImpl(
    context: Context
) : Database {
    private val db = Room
        .databaseBuilder(context, AppDatabase::class.java, "database-app")
        .fallbackToDestructiveMigration()
        .build()

    override suspend fun getFoldersByTypeLocation(param: GetFoldersByTypeLocationParam): List<Folder> {
        val list = db.folderDao().getByTypeLocation(typeLocation = param.location)
        return list
    }

    override suspend fun saveNewFolder(folder: Folder): Long {
        return db.folderDao().insert(folder = folder)
    }

    override suspend fun getFolderById(params: GetFolderByIdParam): Folder {
        return db.folderDao().getById(idFolder = params.idFolder)
    }

    override suspend fun getFoldersByLocation(param: LocationParam): List<Folder> {
        return db.folderDao().getByLocation(typeLocation = param.typeLocation, idLocation = param.idLocation)
    }

    override suspend fun getFoldersWithCountsSubElementsByLocation(param: LocationParam): List<FolderExt> {
        return db.folderDao().getWithCountsInternalElements(param.typeLocation, param.idLocation)
    }

    override suspend fun saveEditFolder(folder: Folder): Int {
        return db.folderDao().update(folder)
    }

    override suspend fun deleteFolder(folder: Folder): Int {
        return db.folderDao().delete(folder)
    }

    override suspend fun deleteFoldersByLocation(param: LocationParam): Int {
        return db.folderDao().deleteByLocation(param.typeLocation, param.idLocation)
    }


    override suspend fun saveNewProject(project: Project): Long {
        return db.projectDao().insert(project)
    }

    override suspend fun saveEditProject(project: Project): Int {
        return db.projectDao().update(project)
    }

    override suspend fun getProjectsByTypeLocation(param: GetProjectsByTypeLocationParam): List<Project> {
        return db.projectDao().getByTypeLocation(param.location)
    }

    override suspend fun getProjectsByLocation(param: LocationParam): List<Project> {
        return db.projectDao().getByLocation(param.typeLocation, param.idLocation)
    }

    override suspend fun getProjectsWithCountsSubElementsByLocation(param: LocationParam): List<ProjectExt> {
        return db.projectDao().getWithCountsInternalElements(param.typeLocation, param.idLocation)
    }

    override suspend fun getProjectById(param: GetProjectByIdParam): Project {
        return db.projectDao().getById(param.idProject)
    }

    override suspend fun deleteProject(project: Project): Int {
        return db.projectDao().delete(project)
    }

    override suspend fun deleteProjectsByLocation(param: LocationParam): Int {
        return db.projectDao().deleteByLocation(param.typeLocation, param.idLocation)
    }

    override suspend fun setCompleteProject(param: SetCompleteParam): Int {
        return db.projectDao().setComplete(param.idElement, param.dateComplete)
    }

    override suspend fun removeCompleteProject(idProject: Long): Int {
        return db.projectDao().removeComplete(idProject)
    }


    override suspend fun saveNewTask(task: Task): Long {
        return db.taskDao().insert(task)
    }

    override suspend fun saveEditTask(task: Task): Int {
        return db.taskDao().update(task)
    }

    override suspend fun getTasksByTypeLocation(param: GetTasksByTypeLocationParam): List<Task> {
        return db.taskDao().getByTypeLocation(param.location)
    }

    override suspend fun getTasksByLocation(param: LocationParam): List<Task> {
        return db.taskDao().getByLocation(param.typeLocation, param.idLocation)
    }

    override suspend fun getTasksWithCountsSubElementsByLocation(param: LocationParam): List<TaskExt> {
        return db.taskDao().getWithCountsInternalElements(param.typeLocation, param.idLocation)
    }

    override suspend fun getTaskById(param: GetTaskByIdParam): Task {
        return db.taskDao().getById(param.idTask)
    }

    override suspend fun deleteTask(task: Task): Int {
        return db.taskDao().delete(task)
    }

    override suspend fun deleteTasksByLocation(param: LocationParam): Int {
        return db.taskDao().deleteByLocation(param.typeLocation, param.idLocation)
    }

    override suspend fun setCompleteTask(param: SetCompleteParam): Int {
        return db.taskDao().setComplete(param.idElement, param.dateComplete)
    }

    override suspend fun removeCompleteTask(idTask: Long): Int {
        return db.taskDao().removeComplete(idTask)
    }
}