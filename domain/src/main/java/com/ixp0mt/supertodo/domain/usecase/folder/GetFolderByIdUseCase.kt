package com.ixp0mt.supertodo.domain.usecase.folder

import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.repository.FolderRepository

class GetFolderByIdUseCase(private val folderRepository: FolderRepository) {
    suspend operator fun invoke(params: GetFolderByIdParam): Result<FolderInfo> {
        return try {
            val response = folderRepository.getById(params)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}