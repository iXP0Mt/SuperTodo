package com.ixp0mt.supertodo.domain.usecase.project

import com.ixp0mt.supertodo.domain.model.GetFoldersByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.GetProjectsByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.repository.ProjectRepository

class GetProjectsByLocationUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(param: LocationParam): Result<List<ProjectInfo>> {
        return try {
            val response =
                if(param.idLocation == 0L) projectRepository.getByTypeLocation(
                    GetProjectsByTypeLocationParam(param.typeLocation)
                )
                else projectRepository.getByLocation(param)

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}