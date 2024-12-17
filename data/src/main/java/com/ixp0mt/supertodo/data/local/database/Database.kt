package com.ixp0mt.supertodo.data.local.database

import com.ixp0mt.supertodo.data.local.database.entity.Folder
import com.ixp0mt.supertodo.data.local.database.entity.Project
import com.ixp0mt.supertodo.data.local.database.entity.Task
import com.ixp0mt.supertodo.data.local.database.tuple.FolderExt
import com.ixp0mt.supertodo.data.local.database.tuple.ProjectExt
import com.ixp0mt.supertodo.data.local.database.tuple.TaskExt
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam

interface Database {
    suspend fun saveNewFolder(folder: Folder): Long
    suspend fun getFolderById(idFolder: Long): Folder
    suspend fun getFoldersByLocation(param: ElementParam): List<Folder>
    suspend fun getFoldersWithCountsSubElementsByLocation(param: ElementParam): List<FolderExt>
    suspend fun saveEditFolder(folder: Folder): Int
    suspend fun deleteFolder(folder: Folder): Int
    suspend fun deleteFoldersByLocation(param: ElementParam): Int


    suspend fun saveNewProject(project: Project): Long
    suspend fun saveEditProject(project: Project): Int
    suspend fun getProjectsByLocation(param: ElementParam): List<Project>
    suspend fun getProjectsWithCountsSubElementsByLocation(param: ElementParam): List<ProjectExt>
    suspend fun getProjectById(idProject: Long): Project
    suspend fun deleteProject(project: Project): Int
    suspend fun deleteProjectsByLocation(param: ElementParam): Int
    suspend fun setCompleteProject(param: SetCompleteParam): Int
    suspend fun removeCompleteProject(idProject: Long): Int


    suspend fun saveNewTask(task: Task): Long
    suspend fun saveEditTask(task: Task): Int
    suspend fun getTasksByLocation(param: ElementParam): List<Task>
    suspend fun getTasksWithCountsSubElementsByLocation(param: ElementParam): List<TaskExt>
    suspend fun getTaskById(idTask: Long): Task
    suspend fun deleteTask(task: Task): Int
    suspend fun deleteTasksByLocation(param: ElementParam): Int
    suspend fun setCompleteTask(param: SetCompleteParam): Int
    suspend fun removeCompleteTask(idTask: Long): Int
}