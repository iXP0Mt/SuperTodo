package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.ResponseDelete
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository

class DeleteElementUseCase(
    private val folderRepository: FolderRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    private var numDeletedFolders = 0
    private var numDeletedProjects = 0
    private var numDeletedTasks = 0

    //suspend fun execute(folder: FolderInfo): Result<ResponseDelete> = getResult { deleteFolder(folder) }
    //suspend fun execute(project: ProjectInfo): Result<ResponseDelete> = getResult { deleteProject(project) }
    //suspend fun execute(task: TaskInfo): Result<ResponseDelete> = getResult { deleteTask(task) }
    suspend fun execute(element: ElementInfo): Result<ResponseDelete> = getResult { deleteElement(element) }

    private suspend fun getResult(deleteAction: suspend () -> Unit): Result<ResponseDelete> {
        return try {
            deleteAction()
            val responseDelete = ResponseDelete(
                numDeletedFolders = numDeletedFolders.takeIf { it > 0 },
                numDeletedProjects = numDeletedProjects.takeIf { it > 0 },
                numDeletedTasks = numDeletedTasks.takeIf { it > 0 }
            )
            Result.success(responseDelete)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun deleteElement(element: ElementInfo) {
        when (element) {
            is FolderInfo -> deleteFolder(element)
            is ProjectInfo -> deleteProject(element)
            is TaskInfo -> deleteTask(element)
        }
    }

    private suspend fun deleteFolder(folder: FolderInfo) {
        val param = LocationParam(TypeLocation.FOLDER, folder.idFolder)

        folderRepository.getByLocation(param).forEach { deleteFolder(it) }
        projectRepository.getByLocation(param).forEach { deleteProject(it) }
        taskRepository.getByLocation(param).forEach { deleteTask(it) }

        folderRepository.delete(folder)
        numDeletedFolders++
    }

    private suspend fun deleteProject(project: ProjectInfo) {
        val param = LocationParam(TypeLocation.PROJECT, project.idProject)

        folderRepository.getByLocation(param).forEach { deleteFolder(it) }
        projectRepository.getByLocation(param).forEach { deleteProject(it) }
        taskRepository.getByLocation(param).forEach { deleteTask(it) }

        projectRepository.delete(project)
        numDeletedProjects++
    }

    private suspend fun deleteTask(task: TaskInfo) {
        val param = LocationParam(TypeLocation.TASK, task.idTask)

        taskRepository.getByLocation(param).forEach { deleteTask(it) }

        taskRepository.delete(task)
        numDeletedTasks++
    }
}