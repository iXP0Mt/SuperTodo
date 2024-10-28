package com.ixp0mt.supertodo.domain.repository

import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.model.GetFoldersByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam

interface FolderRepository {
    suspend fun saveNew(folder: FolderInfo): Long
    suspend fun saveEdit(folder: FolderInfo): Int
    suspend fun delete(folder: FolderInfo): Int
    suspend fun getByTypeLocation(param: GetFoldersByTypeLocationParam): List<FolderInfo>
    suspend fun getById(param: GetFolderByIdParam): FolderInfo
    suspend fun getByLocation(param: LocationParam): List<FolderInfo>
    suspend fun deleteByLocation(param: LocationParam): Int
}