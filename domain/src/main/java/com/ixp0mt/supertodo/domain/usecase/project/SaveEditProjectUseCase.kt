package com.ixp0mt.supertodo.domain.usecase.project

import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.repository.ProjectRepository

class SaveEditProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend fun execute(project: ProjectInfo): Result<Int> {
        return try {
            val validProject = project.copy(name = project.name.trim())
            val response = projectRepository.saveEdit(validProject)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}