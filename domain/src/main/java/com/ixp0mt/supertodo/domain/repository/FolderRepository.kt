package com.ixp0mt.supertodo.domain.repository

import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.folder.FolderInfo
import com.ixp0mt.supertodo.domain.model.folder.FolderWithCounters

interface FolderRepository {
    suspend fun saveNew(folder: FolderInfo): Long
    suspend fun saveEdit(folder: FolderInfo): Int
    suspend fun delete(folder: FolderInfo): Int
    suspend fun getById(idFolder: Long): FolderInfo
    suspend fun getByLocation(param: ElementParam): List<FolderInfo>
    suspend fun getWithCountsSubElementsByLocation(param: ElementParam): List<FolderWithCounters>
    suspend fun deleteByLocation(param: ElementParam): Int
}