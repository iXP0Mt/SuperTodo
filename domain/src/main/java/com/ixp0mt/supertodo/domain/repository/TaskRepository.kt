package com.ixp0mt.supertodo.domain.repository

import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.GetTasksByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.model.TaskInfo

interface TaskRepository {
    suspend fun saveNew(task: TaskInfo): Long
    suspend fun saveEdit(task: TaskInfo): Int
    suspend fun getByTypeLocation(param: GetTasksByTypeLocationParam): List<TaskInfo>
    suspend fun getByLocation(param: LocationParam): List<TaskInfo>
    suspend fun getById(param: GetTaskByIdParam): TaskInfo
    suspend fun delete(task: TaskInfo): Int
    suspend fun deleteByLocation(param: LocationParam): Int
    suspend fun setComplete(param: SetCompleteParam): Int
    suspend fun removeComplete(idTask: Long): Int
}