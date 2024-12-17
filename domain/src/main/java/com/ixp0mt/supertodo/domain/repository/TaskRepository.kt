package com.ixp0mt.supertodo.domain.repository

import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.model.task.TaskInfo
import com.ixp0mt.supertodo.domain.model.task.TaskWithCounters

interface TaskRepository {
    suspend fun saveNew(task: TaskInfo): Long
    suspend fun saveEdit(task: TaskInfo): Int
    suspend fun getByLocation(param: ElementParam): List<TaskInfo>
    suspend fun getById(idTask: Long): TaskInfo
    suspend fun delete(task: TaskInfo): Int
    suspend fun deleteByLocation(param: ElementParam): Int
    suspend fun getWithCountsSubElementsByLocation(param: ElementParam): List<TaskWithCounters>
    suspend fun setComplete(param: SetCompleteParam): Int
    suspend fun removeComplete(idTask: Long): Int
}