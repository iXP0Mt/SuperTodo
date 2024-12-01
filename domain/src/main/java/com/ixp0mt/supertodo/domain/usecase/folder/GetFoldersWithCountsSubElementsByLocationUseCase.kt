package com.ixp0mt.supertodo.domain.usecase.folder

import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.repository.FolderRepository

class GetFoldersWithCountsSubElementsByLocationUseCase(private val folderRepository: FolderRepository) {
    suspend operator fun invoke(param: LocationParam): Result<List<FolderInfo>> {
        return try {
            val response = folderRepository.getWithCountsSubElementsByLocation(param)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}