package com.ixp0mt.supertodo.domain.usecase.task

import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.repository.TaskRepository

class GetTasksWithCountsSubElementsByLocationUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(param: LocationParam): Result<List<TaskInfo>> {
        return try {
            val response = taskRepository.getWithCountsSubElementsByLocation(param)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}