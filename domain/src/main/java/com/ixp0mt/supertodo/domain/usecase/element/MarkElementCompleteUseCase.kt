package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.MarkElementCompleteParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.util.TypeElement

class MarkElementCompleteUseCase(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(param: MarkElementCompleteParam): Boolean {
        val markElement = when(param.typeElement) {
            TypeElement.PROJECT -> MarkProject(projectRepository)
            TypeElement.TASK -> MarkTask(taskRepository)
            else -> return false
        }

        if(!param.isComplete) {
            val newDateComplete = System.currentTimeMillis()
            val lParam = SetCompleteParam(param.idElement, newDateComplete)

            markElement.setCompleteDB(lParam)
        } else {
            markElement.removeCompleteDB(param.idElement)
        }

        return true
    }


    private interface MarkElement {
        suspend fun setCompleteDB(param: SetCompleteParam)
        suspend fun removeCompleteDB(idElement: Long)
    }

    private class MarkProject(private val projectRepository: ProjectRepository) : MarkElement {
        override suspend fun setCompleteDB(param: SetCompleteParam) {
            projectRepository.setComplete(param)
        }

        override suspend fun removeCompleteDB(idElement: Long) {
            projectRepository.removeComplete(idElement)
        }
    }

    private class MarkTask(private val taskRepository: TaskRepository) : MarkElement {
        override suspend fun setCompleteDB(param: SetCompleteParam) {
            taskRepository.setComplete(param)
        }

        override suspend fun removeCompleteDB(idElement: Long) {
            taskRepository.removeComplete(idElement)
        }
    }
}
