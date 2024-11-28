package com.ixp0mt.supertodo.domain.usecase.folder

import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.repository.FolderRepository

class SaveEditFolderUseCase(private val folderRepository: FolderRepository) {
    suspend operator fun invoke(folder: FolderInfo): Result<Int> {
        return try {
            val validFolder = folder.copy(name = folder.name.trim())
            val response = folderRepository.saveEdit(validFolder)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}