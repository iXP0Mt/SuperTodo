package com.ixp0mt.supertodo.data.repository

import com.ixp0mt.supertodo.data.local.database.Database
import com.ixp0mt.supertodo.data.local.database.entity.Folder
import com.ixp0mt.supertodo.data.local.database.tuple.FolderExt
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.model.GetFoldersByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ValuesElementsInfo
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.util.TypeLocation

class FolderRepositoryImpl(private val database: Database) : FolderRepository {

    override suspend fun getByTypeLocation(param: GetFoldersByTypeLocationParam): List<FolderInfo> {
        val response = database.getFoldersByTypeLocation(param)
        val result = response.toDomain()
        return result
    }

    override suspend fun saveNew(folder: FolderInfo): Long  {
        val param = folder.toData()
        val response = database.saveNewFolder(param)
        return response
    }

    override suspend fun getById(param: GetFolderByIdParam): FolderInfo {
        val response = database.getFolderById(param)
        val result = response.toDomain()
        return result
    }

    override suspend fun getByLocation(param: LocationParam): List<FolderInfo> {
        val response = database.getFoldersByLocation(param)
        val result = response.toDomain()
        return result
    }

    override suspend fun getWithCountsSubElementsByLocation(param: LocationParam): List<FolderInfo> {
        val response = database.getFoldersWithCountsSubElementsByLocation(param)
        val result = response.toDomain2()
        return result
    }

    override suspend fun deleteByLocation(param: LocationParam): Int {
        return database.deleteFoldersByLocation(param)
    }

    override suspend fun saveEdit(folder: FolderInfo): Int {
        val param = folder.toData()
        val response = database.saveEditFolder(param)
        return response
    }

    override suspend fun delete(folder: FolderInfo): Int {
        val param = folder.toData()
        val response = database.deleteFolder(param)
        return response
    }




    private fun FolderInfo.toData(): Folder {
        return Folder(
            idFolder = this.idFolder,
            name = this.name,
            description = this.description,
            typeLocation = this.typeLocation,
            idLocation = this.idLocation,
            dateCreate = this.dateCreate,
            dateEdit = this.dateEdit,
            dateArchive = this.dateArchive
        )
    }

    private fun List<Folder>.toDomain(): List<FolderInfo> {
        return this.map {
            FolderInfo(
                idFolder = it.idFolder,
                name = it.name,
                description = it.description,
                typeLocation = it.typeLocation,
                idLocation = it.idLocation ?: 0,
                dateCreate = it.dateCreate,
                dateEdit = it.dateEdit,
                dateArchive = it.dateArchive
            )
        }
    }

    private fun Folder.toDomain(): FolderInfo {
        return FolderInfo(
            idFolder = this.idFolder,
            name = this.name,
            description = this.description,
            typeLocation = this.typeLocation,
            idLocation = this.idLocation ?: 0,
            dateCreate = this.dateCreate,
            dateEdit = this.dateEdit,
            dateArchive = this.dateArchive
        )
    }

    //@Deprecated("Локация это костыль", level = DeprecationLevel.WARNING)
    // Жалуется на то, что компилятор не может распознать разные toDomain, потому что не учитывает входной тип
    private fun List<FolderExt>.toDomain2(): List<FolderInfo> {
        return this.map {
            FolderInfo(
                idFolder = it.idFolder,
                name = it.name,
                description = null,
                typeLocation = TypeLocation.MAIN,
                idLocation = 0,
                dateCreate = it.dateCreate,
                dateEdit = it.dateEdit,
                dateArchive = null,
                countsSubElements = ValuesElementsInfo(
                    it.countSubFolders,
                    it.countSubProjects,
                    it.countSubTasks
                )
            )
        }
    }

}