package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.model.element.IElementMeta
import com.ixp0mt.supertodo.domain.model.meta.MetaElement
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.util.TypeElement

class GetListNamesPedigreeUseCase(
    private val folderRepository: FolderRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    private lateinit var listNamesLocations: MutableList<String>

    suspend operator fun invoke(element: IElement): List<String> {
        listNamesLocations = mutableListOf()
        handleListPedigree(element.typeLocation, element.idLocation)
        return listNamesLocations
    }

    private suspend fun handleListPedigree(typeLocation: TypeElement, idLocation: Long) {
        val element: IElementMeta = when (typeLocation) {
            TypeElement.FOLDER -> folderRepository.getById(idLocation)
            TypeElement.PROJECT -> projectRepository.getById(idLocation)
            TypeElement.TASK -> taskRepository.getById(idLocation)
            TypeElement.MAIN -> {
                listNamesLocations.add(MetaElement.MainFolder.name)
                return
            }
            else -> {
                listNamesLocations.add("NULL")
                return
            }
        }

        element as IElement
        handleListPedigree(
            typeLocation = element.typeLocation,
            idLocation = element.idLocation
        )

        listNamesLocations.add(element.name)
    }
}