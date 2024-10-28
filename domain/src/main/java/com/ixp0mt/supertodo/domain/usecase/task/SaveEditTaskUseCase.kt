package com.ixp0mt.supertodo.domain.usecase.task

import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.repository.TaskRepository

class SaveEditTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: TaskInfo): Result<Int> {
        return try {
            val validTask = task.copy(name = task.name.trim())
            val response = taskRepository.saveEdit(validTask)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}