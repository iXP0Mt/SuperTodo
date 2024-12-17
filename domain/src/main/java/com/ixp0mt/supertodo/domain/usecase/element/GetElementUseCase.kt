package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.element.IElementMeta
import com.ixp0mt.supertodo.domain.model.folder.FolderInfo
import com.ixp0mt.supertodo.domain.model.meta.MetaElement
import com.ixp0mt.supertodo.domain.model.project.ProjectInfo
import com.ixp0mt.supertodo.domain.model.task.TaskInfo
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository

abstract class GetElementUseCase {
    suspend operator fun invoke(idElement: Long): IElementMeta {
        return if(idElement == 0L) {
            getEmptyElement()
        } else {
            loadFromDB(idElement)
        }
    }

    protected abstract suspend fun loadFromDB(idElement: Long): IElementMeta

    protected abstract fun getEmptyElement(): IElementMeta
}


class GetMainFolderUseCase : GetElementUseCase() {
    override suspend fun loadFromDB(idElement: Long): IElementMeta {
        return MetaElement.MainFolder
    }

    override fun getEmptyElement(): IElementMeta {
        return MetaElement.MainFolder
    }
}

class GetFolderUseCase(private val folderRepository: FolderRepository) : GetElementUseCase() {
    override suspend fun loadFromDB(idElement: Long): IElementMeta {
        return folderRepository.getById(idElement)
    }

    override fun getEmptyElement(): IElementMeta {
        return FolderInfo()
    }
}

class GetProjectUseCase(private val projectRepository: ProjectRepository) : GetElementUseCase() {
    override suspend fun loadFromDB(idElement: Long): IElementMeta {
        return projectRepository.getById(idElement)
    }

    override fun getEmptyElement(): IElementMeta {
        return ProjectInfo()
    }
}

class GetTaskUseCase(private val taskRepository: TaskRepository) : GetElementUseCase() {
    override suspend fun loadFromDB(idElement: Long): IElementMeta {
        return taskRepository.getById(idElement)
    }

    override fun getEmptyElement(): IElementMeta {
        return TaskInfo()
    }
}

