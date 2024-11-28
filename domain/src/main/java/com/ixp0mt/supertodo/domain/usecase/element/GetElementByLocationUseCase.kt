package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.util.TypeLocation

class GetElementByLocationUseCase(
    private val folderRepository: FolderRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(param: LocationParam): Result<ElementInfo> {
        return try {
            val element = getElement(param)
            Result.success(element)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getElement(param: LocationParam): ElementInfo {
        return when(param.typeLocation) {
            TypeLocation.FOLDER -> folderRepository.getById(GetFolderByIdParam(param.idLocation))
            TypeLocation.PROJECT -> projectRepository.getById(GetProjectByIdParam(param.idLocation))
            TypeLocation.TASK -> taskRepository.getById(GetTaskByIdParam(param.idLocation))
            TypeLocation.MAIN -> FolderInfo.main()
        }
    }
}