package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.ResponseDelete
import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.model.folder.FolderInfo
import com.ixp0mt.supertodo.domain.model.project.ProjectInfo
import com.ixp0mt.supertodo.domain.model.task.TaskInfo
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.util.TypeElement

class DeleteElementUseCase(
    private val folderRepository: FolderRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    private var numDeletedFolders = 0
    private var numDeletedProjects = 0
    private var numDeletedTasks = 0


    suspend operator fun invoke(element: IElement): ResponseDelete {
        deleteElement(element)
        return ResponseDelete(
            numDeletedFolders = numDeletedFolders.takeIf { it > 0 },
            numDeletedProjects = numDeletedProjects.takeIf { it > 0 },
            numDeletedTasks = numDeletedTasks.takeIf { it > 0 }
        )
    }

    private suspend fun deleteElement(element: IElement) {
        when (element) {
            is FolderInfo -> deleteFolder(element)
            is ProjectInfo -> deleteProject(element)
            is TaskInfo -> deleteTask(element)
        }
    }

    private suspend fun deleteFolder(folder: FolderInfo) {
        val param = ElementParam(TypeElement.FOLDER, folder.id)

        folderRepository.getByLocation(param).forEach { deleteFolder(it) }
        projectRepository.getByLocation(param).forEach { deleteProject(it) }
        taskRepository.getByLocation(param).forEach { deleteTask(it) }

        folderRepository.delete(folder)
        numDeletedFolders++
    }

    private suspend fun deleteProject(project: ProjectInfo) {
        val param = ElementParam(TypeElement.PROJECT, project.id)

        folderRepository.getByLocation(param).forEach { deleteFolder(it) }
        projectRepository.getByLocation(param).forEach { deleteProject(it) }
        taskRepository.getByLocation(param).forEach { deleteTask(it) }

        projectRepository.delete(project)
        numDeletedProjects++
    }

    private suspend fun deleteTask(task: TaskInfo) {
        val param = ElementParam(TypeElement.TASK, task.id)

        taskRepository.getByLocation(param).forEach { deleteTask(it) }

        taskRepository.delete(task)
        numDeletedTasks++
    }
}