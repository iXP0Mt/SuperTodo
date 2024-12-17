package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.util.TypeElement

class GetSubElementsWithCountersUseCase(
    private val folderRepository: FolderRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(parentTypeElement: TypeElement, parentIdElement: Long): List<IElement> {

        val listSubElements = mutableListOf<IElement>()

        val param = ElementParam(parentTypeElement, parentIdElement)

        if(parentTypeElement != TypeElement.TASK) {
            listSubElements += folderRepository.getWithCountsSubElementsByLocation(param)
            listSubElements += projectRepository.getWithCountsSubElementsByLocation(param)
        }
        listSubElements += taskRepository.getWithCountsSubElementsByLocation(param)

        return listSubElements
    }
}