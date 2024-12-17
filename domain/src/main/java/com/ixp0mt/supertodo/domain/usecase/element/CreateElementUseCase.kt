package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.model.folder.FolderInfo
import com.ixp0mt.supertodo.domain.model.project.ProjectInfo
import com.ixp0mt.supertodo.domain.model.task.TaskInfo
import com.ixp0mt.supertodo.domain.util.TypeElement

interface CreateElementUseCase {
    operator fun invoke(
        id: Long,
        name: String,
        description: String?,
        typeLocation: TypeElement,
        idLocation: Long,
        dateCreate: Long,
        dateEdit: Long?,
        dateArchive: Long?
    ): IElement
}

class CreateFolderUseCase : CreateElementUseCase {
    override fun invoke(
        id: Long,
        name: String,
        description: String?,
        typeLocation: TypeElement,
        idLocation: Long,
        dateCreate: Long,
        dateEdit: Long?,
        dateArchive: Long?
    ): IElement {
        return FolderInfo(
            id = id,
            name = name,
            description = description,
            typeLocation = typeLocation,
            idLocation = idLocation,
            dateCreate = dateCreate,
            dateEdit = dateEdit,
            dateArchive = dateArchive
        )
    }
}

class CreateProjectUseCase : CreateElementUseCase {
    override fun invoke(
        id: Long,
        name: String,
        description: String?,
        typeLocation: TypeElement,
        idLocation: Long,
        dateCreate: Long,
        dateEdit: Long?,
        dateArchive: Long?
    ): IElement {
        return ProjectInfo(
            id = id,
            name = name,
            description = description,
            typeLocation = typeLocation,
            idLocation = idLocation,
            dateCreate = dateCreate,
            dateEdit = dateEdit,
            dateArchive = dateArchive,
            datePlanStart = null,
            datePlanEnd = null,
            dateFactEnd = null
        )
    }
}

class CreateTaskUseCase : CreateElementUseCase {
    override fun invoke(
        id: Long,
        name: String,
        description: String?,
        typeLocation: TypeElement,
        idLocation: Long,
        dateCreate: Long,
        dateEdit: Long?,
        dateArchive: Long?
    ): IElement {
        return TaskInfo(
            id = id,
            name = name,
            description = description,
            typeLocation = typeLocation,
            idLocation = idLocation,
            dateCreate = dateCreate,
            dateEdit = dateEdit,
            dateArchive = dateArchive,
            datePlanStart = null,
            datePlanEnd = null,
            dateFactEnd = null
        )
    }
}