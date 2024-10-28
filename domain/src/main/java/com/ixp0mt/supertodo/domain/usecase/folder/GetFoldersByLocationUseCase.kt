package com.ixp0mt.supertodo.domain.usecase.folder

import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.GetFoldersByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.repository.FolderRepository

class GetFoldersByLocationUseCase(private val folderRepository: FolderRepository) {
    suspend fun execute(param: LocationParam): Result<List<FolderInfo>> {
        return try {
            val response =
                if(param.idLocation == 0L) folderRepository.getByTypeLocation(
                    GetFoldersByTypeLocationParam(param.typeLocation)
                )
                else folderRepository.getByLocation(param)

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}