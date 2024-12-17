package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.element.IElementMeta
import com.ixp0mt.supertodo.domain.model.meta.MetaElement
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.util.TypeElement

class GetLocationAsElementUseCase(
    private val folderRepository: FolderRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(param: ElementParam): IElementMeta {
        return when(param.typeElement) {
            TypeElement.DEFAULT -> MetaElement.Default
            TypeElement.MAIN -> MetaElement.MainFolder
            TypeElement.FOLDER -> folderRepository.getById(param.idElement)
            TypeElement.PROJECT -> projectRepository.getById(param.idElement)
            TypeElement.TASK -> taskRepository.getById(param.idElement)
        }
    }
}