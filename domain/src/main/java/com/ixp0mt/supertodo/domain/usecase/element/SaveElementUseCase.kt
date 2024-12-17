package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.model.folder.FolderInfo
import com.ixp0mt.supertodo.domain.model.project.ProjectInfo
import com.ixp0mt.supertodo.domain.model.task.TaskInfo
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository

interface SaveElementUseCase {
    suspend operator fun invoke(element: IElement): Long
}

class SaveNewFolderUseCase(private val folderRepository: FolderRepository) : SaveElementUseCase {
    override suspend fun invoke(element: IElement): Long {
        return folderRepository.saveNew(element as FolderInfo)
    }
}

class SaveEditFolderUseCase(private val folderRepository: FolderRepository) : SaveElementUseCase {
    override suspend fun invoke(element: IElement): Long {
        folderRepository.saveEdit(element as FolderInfo)
        return -1
    }
}

class SaveNewProjectUseCase(private val projectRepository: ProjectRepository) : SaveElementUseCase {
    override suspend fun invoke(element: IElement): Long {
        return projectRepository.saveNew(element as ProjectInfo)
    }
}

class SaveEditProjectUseCase(private val projectRepository: ProjectRepository) : SaveElementUseCase {
    override suspend fun invoke(element: IElement): Long {
        projectRepository.saveEdit(element as ProjectInfo)
        return -1
    }
}

class SaveNewTaskUseCase(private val taskRepository: TaskRepository) : SaveElementUseCase {
    override suspend fun invoke(element: IElement): Long {
        return taskRepository.saveNew(element as TaskInfo)
    }
}

class SaveEditTaskUseCase(private val taskRepository: TaskRepository) : SaveElementUseCase {
    override suspend fun invoke(element: IElement): Long {
        taskRepository.saveEdit(element as TaskInfo)
        return -1
    }
}





