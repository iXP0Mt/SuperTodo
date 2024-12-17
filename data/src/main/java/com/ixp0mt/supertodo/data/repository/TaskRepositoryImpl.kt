package com.ixp0mt.supertodo.data.repository

import com.ixp0mt.supertodo.data.local.database.Database
import com.ixp0mt.supertodo.data.local.database.entity.Task
import com.ixp0mt.supertodo.data.local.database.tuple.TaskExt
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.model.task.TaskInfo
import com.ixp0mt.supertodo.domain.model.task.TaskWithCounters
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.util.TypeElement

class TaskRepositoryImpl(private val database: Database) : TaskRepository {
    override suspend fun saveNew(task: TaskInfo): Long {
        return database.saveNewTask(task.toData())
    }

    override suspend fun saveEdit(task: TaskInfo): Int {
        return database.saveEditTask(task.toData())
    }

    override suspend fun getByLocation(param: ElementParam): List<TaskInfo> {
        return database.getTasksByLocation(param).toDomain()
    }

    override suspend fun getById(idTask: Long): TaskInfo {
        return database.getTaskById(idTask).toDomain()
    }

    override suspend fun delete(task: TaskInfo): Int {
        return database.deleteTask(task.toData())
    }

    override suspend fun deleteByLocation(param: ElementParam): Int {
        return database.deleteTasksByLocation(param)
    }

    override suspend fun getWithCountsSubElementsByLocation(param: ElementParam): List<TaskWithCounters> {
        val response = database.getTasksWithCountsSubElementsByLocation(param)
        val result = response.toDomain2()
        return result
    }

    override suspend fun setComplete(param: SetCompleteParam): Int {
        return database.setCompleteTask(param)
    }

    override suspend fun removeComplete(idTask: Long): Int {
        return database.removeCompleteTask(idTask)
    }


    private fun Task.toDomain() = TaskInfo(
        id = idTask,
        name = name,
        description = description,
        typeLocation = typeLocation,
        idLocation = idLocation ?: 0,
        dateCreate = dateCreate,
        dateEdit = dateEdit,
        dateArchive = dateArchive,
        datePlanStart = dateStart,
        datePlanEnd = dateEnd,
        dateFactEnd = dateCompleted
    )

    private fun List<Task>.toDomain() = this.map {
        TaskInfo(
            id = it.idTask,
            name = it.name,
            description = it.description,
            typeLocation = it.typeLocation,
            idLocation = it.idLocation ?: 0,
            dateCreate = it.dateCreate,
            dateEdit = it.dateEdit,
            dateArchive = it.dateArchive,
            datePlanStart = it.dateStart,
            datePlanEnd = it.dateEnd,
            dateFactEnd = it.dateCompleted
        )
    }

    private fun TaskInfo.toData() = Task(
        idTask = id,
        name = name,
        description = description,
        typeLocation = typeLocation,
        idLocation = idLocation,
        dateCreate = dateCreate,
        dateEdit = dateEdit,
        dateArchive = dateArchive,
        dateStart = datePlanStart,
        dateEnd = datePlanEnd,
        dateCompleted = dateFactEnd
    )

    @Deprecated("Локация это костыль", level = DeprecationLevel.WARNING)
    // Жалуется на то, что компилятор не может распознать разные toDomain, потому что не учитывает входной тип
    private fun List<TaskExt>.toDomain2(): List<TaskWithCounters> {
        return this.map {
            TaskWithCounters(
                task = TaskInfo(
                    id = it.idTask,
                    name = it.name,
                    description = null,
                    typeLocation = TypeElement.DEFAULT,
                    idLocation = 0,
                    dateCreate = it.dateCreate,
                    dateEdit = it.dateEdit,
                    dateArchive = null,
                    datePlanStart = null,
                    datePlanEnd = null,
                    dateFactEnd = it.dateCompleted
                ),
                countSubTasks = it.countSubTasks
            )
        }
    }
}