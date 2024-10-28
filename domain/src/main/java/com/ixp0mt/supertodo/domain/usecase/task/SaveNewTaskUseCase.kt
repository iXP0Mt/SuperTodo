package com.ixp0mt.supertodo.domain.usecase.task

import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.repository.TaskRepository

class SaveNewTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: TaskInfo): Result<Long> {
        return try {
            val validTask = task.copy(name = task.name.trim())
            val response = taskRepository.saveNew(validTask)
            Result.success(response)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}