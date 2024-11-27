package com.ixp0mt.supertodo.domain.usecase.task

import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.repository.TaskRepository

class MarkCompleteTaskUseCase(private val taskRepository: TaskRepository) {
    /**
     * Отметить задачу выполненной или, наоборот, снять метку.
     *
     * @param task Задача
     *
     * @return Объект Result<Long>, который возвращает результат с датой завершения задачи в виде Long или, если метка, наоборот, снята, null
     */
    suspend operator fun invoke(task: TaskInfo): Result<Long?> {
        return try {

            val newDateCompleted: Long?
            val response: Int

            if(task.dateCompleted == null) {
                newDateCompleted = System.currentTimeMillis()
                val param = SetCompleteParam(task.idTask, newDateCompleted)
                response = taskRepository.setComplete(param)
            } else {
                response = taskRepository.removeComplete(task.idTask)
                newDateCompleted = null
            }

            if(response == 0) Result.failure(Exception("Никакая строка не была затронута"))
            else Result.success(newDateCompleted)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}