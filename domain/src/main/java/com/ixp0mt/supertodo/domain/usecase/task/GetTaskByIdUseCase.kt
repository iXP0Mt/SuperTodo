package com.ixp0mt.supertodo.domain.usecase.task

import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.repository.TaskRepository

class GetTaskByIdUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(param: GetTaskByIdParam): Result<TaskInfo> {
        return try {
            val response = taskRepository.getById(param)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}