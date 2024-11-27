package com.ixp0mt.supertodo.domain.usecase.project

import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.repository.ProjectRepository

class GetProjectByIdUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(param: GetProjectByIdParam): Result<ProjectInfo> {
        return try {
            val response = projectRepository.getById(param)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}