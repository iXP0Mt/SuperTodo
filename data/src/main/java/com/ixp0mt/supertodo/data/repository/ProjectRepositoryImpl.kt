package com.ixp0mt.supertodo.data.repository

import com.ixp0mt.supertodo.data.local.database.Database
import com.ixp0mt.supertodo.data.local.database.entity.Project
import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.GetProjectsByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.repository.ProjectRepository

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


    private fun Project.toDomain() = ProjectInfo(
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

    private fun List<Project>.toDomain() = this.map {
        ProjectInfo(
            idProject = it.idProject,
            name = it.name,
            description = it.description,
            typeLocation = it.typeLocation,
            idLocation = it.idLocation,
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
}