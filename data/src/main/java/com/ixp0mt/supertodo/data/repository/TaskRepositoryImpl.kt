package com.ixp0mt.supertodo.data.repository

import com.ixp0mt.supertodo.data.local.database.Database
import com.ixp0mt.supertodo.data.local.database.entity.Task
import com.ixp0mt.supertodo.data.local.database.tuple.ProjectExt
import com.ixp0mt.supertodo.data.local.database.tuple.TaskExt
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.GetTasksByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.model.ValuesElementsInfo
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.util.TypeLocation

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

    override suspend fun getWithCountsSubElementsByLocation(param: LocationParam): List<TaskInfo> {
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

    @Deprecated("Локация это костыль", level = DeprecationLevel.WARNING)
    // Жалуется на то, что компилятор не может распознать разные toDomain, потому что не учитывает входной тип
    private fun List<TaskExt>.toDomain2(): List<TaskInfo> {
        return this.map {
            TaskInfo(
                idTask = it.idTask,
                name = it.name,
                description = null,
                typeLocation = TypeLocation.MAIN,
                idLocation = 0,
                dateCreate = it.dateCreate,
                dateEdit = it.dateEdit,
                dateArchive = null,
                dateStart = null,
                dateEnd = null,
                dateCompleted = it.dateCompleted,
                countsSubElements = ValuesElementsInfo(
                    it.countSubFolders,
                    it.countSubProjects,
                    it.countSubTasks
                )
            )
        }
    }
}