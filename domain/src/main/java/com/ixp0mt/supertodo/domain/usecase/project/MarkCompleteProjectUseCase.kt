package com.ixp0mt.supertodo.domain.usecase.project

import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.repository.ProjectRepository

class MarkCompleteProjectUseCase(private val projectRepository: ProjectRepository) {
    /**
     * Отметить проект выполненным или, наоборот, снять метку.
     *
     * @param project Проект
     *
     * @return Объект Result<Long>, который возвращает результат с датой завершения проекта в виде Long или, если метка, наоборот, снята, null
     */
    suspend operator fun invoke(project: ProjectInfo): Result<Long?> {
        return try {

            val newDateCompleted: Long?
            val response: Int

            if(project.dateCompleted == null) {
                newDateCompleted = System.currentTimeMillis()
                val param = SetCompleteParam(project.idProject, newDateCompleted)
                response = projectRepository.setComplete(param)
            } else {
                response = projectRepository.removeComplete(project.idProject)
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