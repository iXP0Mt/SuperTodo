package com.ixp0mt.supertodo.data.repository

import com.ixp0mt.supertodo.data.local.database.Database
import com.ixp0mt.supertodo.data.local.database.entity.Task
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.GetTasksByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.repository.TaskRepository

class TaskRepositoryImpl(private val database: Database) : TaskRepository {
    override suspend fun saveNew(task: TaskInfo): Long {
        return database.saveNewTask(task.toData())
    }

    override suspend fun saveEdit(task: TaskInfo): Int {
        return database.saveEditTask(task.toData())
    }

    override suspend fun getByTypeLocation(param: GetTasksByTypeLocationParam): List<TaskInfo> {
        return database.getTasksByTypeLocation(param).toDomain()
    }

    override suspend fun getByLocation(param: LocationParam): List<TaskInfo> {
        return database.getTasksByLocation(param).toDomain()
    }

    override suspend fun getById(param: GetTaskByIdParam): TaskInfo {
        return database.getTaskById(param).toDomain()
    }

    override suspend fun delete(task: TaskInfo): Int {
        return database.deleteTask(task.toData())
    }

    override suspend fun deleteByLocation(param: LocationParam): Int {
        return database.deleteTasksByLocation(param)
    }

    override suspend fun setComplete(param: SetCompleteParam): Int {
        return database.setCompleteTask(param)
    }

    override suspend fun removeComplete(idTask: Long): Int {
        return database.removeCompleteTask(idTask)
    }


    private fun Task.toDomain() = TaskInfo(
        idTask = idTask,
        name = name,
        description = description,
        typeLocation = typeLocation,
        idLocation = idLocation ?: 0,
        dateCreate = dateCreate,
        dateEdit = dateEdit,
        dateArchive = dateArchive,
        dateStart = dateStart,
        dateEnd = dateEnd,
        dateCompleted = dateCompleted
    )

    private fun List<Task>.toDomain() = this.map {
        TaskInfo(
            idTask = it.idTask,
            name = it.name,
            description = it.description,
            typeLocation = it.typeLocation,
            idLocation = it.idLocation ?: 0,
            dateCreate = it.dateCreate,
            dateEdit = it.dateEdit,
            dateArchive = it.dateArchive,
            dateStart = it.dateStart,
            dateEnd = it.dateEnd,
            dateCompleted = it.dateCompleted
        )
    }

    private fun TaskInfo.toData() = Task(
        idTask = idTask,
        name = name,
        description = description,
        typeLocation = typeLocation,
        idLocation = idLocation,
        dateCreate = dateCreate,
        dateEdit = dateEdit,
        dateArchive = dateArchive,
        dateStart = dateStart,
        dateEnd = dateEnd,
        dateCompleted = dateCompleted
    )
}