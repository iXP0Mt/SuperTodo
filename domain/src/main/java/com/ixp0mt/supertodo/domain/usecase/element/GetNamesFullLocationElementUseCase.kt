package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.util.TypeLocation

class GetNamesFullLocationElementUseCase(
    private val folderRepository: FolderRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    private lateinit var listNamesLocations: MutableList<String>

    suspend operator fun invoke(param: LocationParam): Result<List<String>> {
        return try {
            listNamesLocations = mutableListOf()
            handleListLocations(param)
            Result.success(listNamesLocations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun handleListLocations(locationParam: LocationParam) {
        val element: ElementInfo = when (locationParam.typeLocation) {
            TypeLocation.FOLDER -> folderRepository.getById(GetFolderByIdParam(locationParam.idLocation))
            TypeLocation.PROJECT -> projectRepository.getById(GetProjectByIdParam(locationParam.idLocation))
            TypeLocation.TASK -> taskRepository.getById(GetTaskByIdParam(locationParam.idLocation))
            TypeLocation.MAIN -> {
                listNamesLocations.add("Главная папка")
                return
            }
            else -> {
                listNamesLocations.add("NULL")
                return
            }
        }

        handleListLocations(LocationParam(
            typeLocation = element.typeLocation,
            idLocation = element.idLocation ?: 0
        ))

        listNamesLocations.add(element.name)
    }
}