package com.ixp0mt.supertodo.domain.repository

import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.GetProjectsByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo

interface ProjectRepository {
    suspend fun saveNew(project: ProjectInfo): Long
    suspend fun saveEdit(project: ProjectInfo): Int
    suspend fun getByTypeLocation(param: GetProjectsByTypeLocationParam): List<ProjectInfo>
    suspend fun getByLocation(param: LocationParam): List<ProjectInfo>
    suspend fun getById(param: GetProjectByIdParam): ProjectInfo
    suspend fun delete(project: ProjectInfo): Int
    suspend fun deleteByLocation(param: LocationParam): Int
}