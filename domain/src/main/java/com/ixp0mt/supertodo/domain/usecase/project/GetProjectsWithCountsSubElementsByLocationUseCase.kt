package com.ixp0mt.supertodo.domain.usecase.project

import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.repository.ProjectRepository

class GetProjectsWithCountsSubElementsByLocationUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(param: LocationParam): Result<List<ProjectInfo>> {
        return try {
            val response = projectRepository.getWithCountsSubElementsByLocation(param)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}