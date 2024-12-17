package com.ixp0mt.supertodo.data.repository

import com.ixp0mt.supertodo.data.local.database.Database
import com.ixp0mt.supertodo.data.local.database.entity.Project
import com.ixp0mt.supertodo.data.local.database.tuple.ProjectExt
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.model.project.ProjectInfo
import com.ixp0mt.supertodo.domain.model.project.ProjectWithCounters
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.util.TypeElement

class ProjectRepositoryImpl(private val database: Database) : ProjectRepository {
    override suspend fun saveNew(project: ProjectInfo): Long {
        val param = project.toData()
        return database.saveNewProject(param)
    }

    override suspend fun saveEdit(project: ProjectInfo): Int {
        val param = project.toData()
        return database.saveEditProject(param)
    }

    override suspend fun getByLocation(param: ElementParam): List<ProjectInfo> {
        val response = database.getProjectsByLocation(param)
        return response.toDomain()
    }

    override suspend fun getById(idProject: Long): ProjectInfo {
        val response = database.getProjectById(idProject)
        return response.toDomain()
    }

    override suspend fun delete(project: ProjectInfo): Int {
        val param = project.toData()
        return database.deleteProject(param)
    }

    override suspend fun deleteByLocation(param: ElementParam): Int {
        return database.deleteProjectsByLocation(param)
    }

    override suspend fun getWithCountsSubElementsByLocation(param: ElementParam): List<ProjectWithCounters> {
        val response = database.getProjectsWithCountsSubElementsByLocation(param)
        val result = response.toDomain2()
        return result
    }

    override suspend fun setComplete(param: SetCompleteParam): Int {
        return database.setCompleteProject(param)
    }

    override suspend fun removeComplete(idProject: Long): Int {
        return database.removeCompleteProject(idProject)
    }


    private fun Project.toDomain() = ProjectInfo(
        id = this.idProject,
        name = this.name,
        description = this.description,
        typeLocation = this.typeLocation,
        idLocation = this.idLocation ?: 0,
        dateCreate = this.dateCreate,
        dateEdit = this.dateEdit,
        dateArchive = this.dateArchive,
        datePlanStart = this.dateStart,
        datePlanEnd = this.dateEnd,
        dateFactEnd = this.dateCompleted
    )

    private fun List<Project>.toDomain() = this.map {
        ProjectInfo(
            id = it.idProject,
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

    private fun ProjectInfo.toData() = Project(
        idProject = this.id,
        name = this.name,
        description = this.description,
        typeLocation = this.typeLocation,
        idLocation = this.idLocation,
        dateCreate = this.dateCreate,
        dateEdit = this.dateEdit,
        dateArchive = this.dateArchive,
        dateStart = this.datePlanStart,
        dateEnd = this.datePlanEnd,
        dateCompleted = this.dateFactEnd
    )

    @Deprecated("Локация это заглушка", level = DeprecationLevel.WARNING)
    // Жалуется на то, что компилятор не может распознать разные toDomain, потому что не учитывает входной тип
    private fun List<ProjectExt>.toDomain2(): List<ProjectWithCounters> {
        return this.map {
            ProjectWithCounters(
                project = ProjectInfo(
                    id = it.idProject,
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
                countSubFolders = it.countSubFolders,
                countSubProjects = it.countSubProjects,
                countSubTasks = it.countSubTasks
            )
        }
    }
}