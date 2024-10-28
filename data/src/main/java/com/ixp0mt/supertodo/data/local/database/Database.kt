package com.ixp0mt.supertodo.data.local.database

import com.ixp0mt.supertodo.data.local.database.entity.Folder
import com.ixp0mt.supertodo.data.local.database.entity.Project
import com.ixp0mt.supertodo.data.local.database.entity.Task
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.model.GetFoldersByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.GetProjectsByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.GetTasksByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam

interface Database {
    suspend fun getFoldersByTypeLocation(param: GetFoldersByTypeLocationParam): List<Folder>
    suspend fun saveNewFolder(folder: Folder): Long
    suspend fun getFolderById(params: GetFolderByIdParam): Folder
    suspend fun getFoldersByLocation(param: LocationParam): List<Folder>
    suspend fun saveEditFolder(folder: Folder): Int
    suspend fun deleteFolder(folder: Folder): Int
    suspend fun deleteFoldersByLocation(param: LocationParam): Int


    suspend fun saveNewProject(project: Project): Long
    suspend fun saveEditProject(project: Project): Int
    suspend fun getProjectsByTypeLocation(param: GetProjectsByTypeLocationParam): List<Project>
    suspend fun getProjectsByLocation(param: LocationParam): List<Project>
    suspend fun getProjectById(param: GetProjectByIdParam): Project
    suspend fun deleteProject(project: Project): Int
    suspend fun deleteProjectsByLocation(param: LocationParam): Int


    suspend fun saveNewTask(task: Task): Long
    suspend fun saveEditTask(task: Task): Int
    suspend fun getTasksByTypeLocation(param: GetTasksByTypeLocationParam): List<Task>
    suspend fun getTasksByLocation(param: LocationParam): List<Task>
    suspend fun getTaskById(param: GetTaskByIdParam): Task
    suspend fun deleteTask(task: Task): Int
    suspend fun deleteTasksByLocation(param: LocationParam): Int
    suspend fun setCompleteTask(param: SetCompleteParam): Int
    suspend fun removeCompleteTask(idTask: Long): Int
}