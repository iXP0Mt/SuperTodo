package com.ixp0mt.supertodo.domain.usecase.task

import com.ixp0mt.supertodo.domain.model.GetProjectsByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.GetTasksByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.repository.TaskRepository

class GetTasksByLocationUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(param: LocationParam): Result<List<TaskInfo>> {
        return try {
            val response =
                if(param.idLocation == 0L) taskRepository.getByTypeLocation(
                    GetTasksByTypeLocationParam(param.typeLocation)
                )
                else taskRepository.getByLocation(param)

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}