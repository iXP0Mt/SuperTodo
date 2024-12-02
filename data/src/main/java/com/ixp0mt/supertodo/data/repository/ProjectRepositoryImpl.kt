package com.ixp0mt.supertodo.data.repository

import com.ixp0mt.supertodo.data.local.database.Database
import com.ixp0mt.supertodo.data.local.database.entity.Project
import com.ixp0mt.supertodo.data.local.database.tuple.ProjectExt
import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.GetProjectsByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.model.ValuesElementsInfo
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.util.TypeLocation

class ProjectRepositoryImpl(private val database: Database) : ProjectRepository {
    override suspend fun saveNew(project: ProjectInfo): Long {
        val param = project.toData()
        return database.saveNewProject(param)
    }

    override suspend fun saveEdit(project: ProjectInfo): Int {
        val param = project.toData()
        return database.saveEditProject(param)
    }

    override suspend fun getByTypeLocation(param: GetProjectsByTypeLocationParam): List<ProjectInfo> {
        val response = database.getProjectsByTypeLocation(param)
        return response.toDomain()
    }

    override suspend fun getByLocation(param: LocationParam): List<ProjectInfo> {
        val response = database.getProjectsByLocation(param)
        return response.toDomain()
    }

    override suspend fun getById(param: GetProjectByIdParam): ProjectInfo {
        val response = database.getProjectById(param)
        return response.toDomain()
    }

    override suspend fun delete(project: ProjectInfo): Int {
        val param = project.toData()
        return database.deleteProject(param)
    }

    override suspend fun deleteByLocation(param: LocationParam): Int {
        return database.deleteProjectsByLocation(param)
    }

    override suspend fun getWithCountsSubElementsByLocation(param: LocationParam): List<ProjectInfo> {
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
        idProject = this.idProject,
        name = this.name,
        description = this.description,
        typeLocation = this.typeLocation,
        idLocation = this.idLocation ?: 0,
        dateCreate = this.dateCreate,
        dateEdit = this.dateEdit,
        dateArchive = this.dateArchive,
        dateStart = this.dateStart,
        dateEnd = this.dateEnd,
        dateCompleted = this.dateCompleted
    )

    private fun List<Project>.toDomain() = this.map {
        ProjectInfo(
            idProject = it.idProject,
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

    private fun ProjectInfo.toData() = Project(
        idProject = this.idProject,
        name = this.name,
        description = this.description,
        typeLocation = this.typeLocation,
        idLocation = this.idLocation,
        dateCreate = this.dateCreate,
        dateEdit = this.dateEdit,
        dateArchive = this.dateArchive,
        dateStart = this.dateStart,
        dateEnd = this.dateEnd,
        dateCompleted = this.dateCompleted
    )

    @Deprecated("Локация это костыль", level = DeprecationLevel.WARNING)
    // Жалуется на то, что компилятор не может распознать разные toDomain, потому что не учитывает входной тип
    private fun List<ProjectExt>.toDomain2(): List<ProjectInfo> {
        return this.map {
            ProjectInfo(
                idProject = it.idProject,
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