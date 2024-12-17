package com.ixp0mt.supertodo.domain.repository

import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.SetCompleteParam
import com.ixp0mt.supertodo.domain.model.project.ProjectInfo
import com.ixp0mt.supertodo.domain.model.project.ProjectWithCounters

interface ProjectRepository {
    suspend fun saveNew(project: ProjectInfo): Long
    suspend fun saveEdit(project: ProjectInfo): Int
    suspend fun getByLocation(param: ElementParam): List<ProjectInfo>
    suspend fun getById(idProject: Long): ProjectInfo
    suspend fun delete(project: ProjectInfo): Int
    suspend fun deleteByLocation(param: ElementParam): Int
    suspend fun getWithCountsSubElementsByLocation(param: ElementParam): List<ProjectWithCounters>
    suspend fun setComplete(param: SetCompleteParam): Int
    suspend fun removeComplete(idProject: Long): Int
}