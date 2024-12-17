package com.ixp0mt.supertodo.data.local.database

import android.content.Context
import androidx.room.Room
import com.ixp0mt.supertodo.data.local.database.entity.Folder
import com.ixp0mt.supertodo.data.local.database.entity.Project
import com.ixp0mt.supertodo.data.local.database.entity.Task
import com.ixp0mt.supertodo.data.local.database.tuple.FolderExt
import com.ixp0mt.supertodo.data.local.database.tuple.ProjectExt
import com.ixp0mt.supertodo.data.local.database.tuple.TaskExt
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam

class DatabaseImpl(
    context: Context
) : Database {
    private val db = Room
        .databaseBuilder(context, AppDatabase::class.java, "database-app")
        .fallbackToDestructiveMigration()
        .build()

    override suspend fun saveNewFolder(folder: Folder): Long {
        return db.folderDao().insert(folder = folder)
    }

    override suspend fun getFolderById(idFolder: Long): Folder {
        return db.folderDao().getById(idFolder = idFolder)
    }

    override suspend fun getFoldersByLocation(param: ElementParam): List<Folder> {
        return db.folderDao().getByLocation(typeLocation = param.typeElement, idLocation = param.idElement)
    }

    override suspend fun getFoldersWithCountsSubElementsByLocation(param: ElementParam): List<FolderExt> {
        return db.folderDao().getWithCountsInternalElements(param.typeElement, param.idElement)
    }

    override suspend fun saveEditFolder(folder: Folder): Int {
        return db.folderDao().update(folder)
    }

    override suspend fun deleteFolder(folder: Folder): Int {
        return db.folderDao().delete(folder)
    }

    override suspend fun deleteFoldersByLocation(param: ElementParam): Int {
        return db.folderDao().deleteByLocation(param.typeElement, param.idElement)
    }


    override suspend fun saveNewProject(project: Project): Long {
        return db.projectDao().insert(project)
    }

    override suspend fun saveEditProject(project: Project): Int {
        return db.projectDao().update(project)
    }

    override suspend fun getProjectsByLocation(param: ElementParam): List<Project> {
        return db.projectDao().getByLocation(param.typeElement, param.idElement)
    }

    override suspend fun getProjectsWithCountsSubElementsByLocation(param: ElementParam): List<ProjectExt> {
        return db.projectDao().getWithCountsInternalElements(param.typeElement, param.idElement)
    }

    override suspend fun getProjectById(idProject: Long): Project {
        return db.projectDao().getById(idProject)
    }

    override suspend fun deleteProject(project: Project): Int {
        return db.projectDao().delete(project)
    }

    override suspend fun deleteProjectsByLocation(param: ElementParam): Int {
        return db.projectDao().deleteByLocation(param.typeElement, param.idElement)
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

    override suspend fun getTasksByLocation(param: ElementParam): List<Task> {
        return db.taskDao().getByLocation(param.typeElement, param.idElement)
    }

    override suspend fun getTasksWithCountsSubElementsByLocation(param: ElementParam): List<TaskExt> {
        return db.taskDao().getWithCountsInternalElements(param.typeElement, param.idElement)
    }

    override suspend fun getTaskById(idTask: Long): Task {
        return db.taskDao().getById(idTask)
    }

    override suspend fun deleteTask(task: Task): Int {
        return db.taskDao().delete(task)
    }

    override suspend fun deleteTasksByLocation(param: ElementParam): Int {
        return db.taskDao().deleteByLocation(param.typeElement, param.idElement)
    }

    override suspend fun setCompleteTask(param: SetCompleteParam): Int {
        return db.taskDao().setComplete(param.idElement, param.dateComplete)
    }

    override suspend fun removeCompleteTask(idTask: Long): Int {
        return db.taskDao().removeComplete(idTask)
    }
}