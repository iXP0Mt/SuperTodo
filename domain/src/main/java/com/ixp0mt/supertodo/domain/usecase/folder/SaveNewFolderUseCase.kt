package com.ixp0mt.supertodo.domain.usecase.folder

import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.repository.FolderRepository

class SaveNewFolderUseCase(private val folderRepository: FolderRepository) {
    suspend operator fun invoke(folder: FolderInfo): Result<Long> {
        return try {
            val validFolder = folder.copy(name = folder.name.trim())
            val response = folderRepository.saveNew(validFolder)
            Result.success(response)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}