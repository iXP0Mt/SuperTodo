package com.ixp0mt.supertodo.data.repository

import com.ixp0mt.supertodo.data.local.database.Database
import com.ixp0mt.supertodo.data.local.database.entity.Folder
import com.ixp0mt.supertodo.data.local.database.tuple.FolderExt
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.folder.FolderInfo
import com.ixp0mt.supertodo.domain.model.folder.FolderWithCounters
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.util.TypeElement

class FolderRepositoryImpl(private val database: Database) : FolderRepository {

    override suspend fun saveNew(folder: FolderInfo): Long  {
        val param = folder.toData()
        val response = database.saveNewFolder(param)
        return response
    }

    override suspend fun getById(idFolder: Long): FolderInfo {
        val response = database.getFolderById(idFolder)
        val result = response.toDomain()
        return result
    }

    override suspend fun getByLocation(param: ElementParam): List<FolderInfo> {
        val response = database.getFoldersByLocation(param)
        val result = response.toDomain()
        return result
    }

    override suspend fun getWithCountsSubElementsByLocation(param: ElementParam): List<FolderWithCounters> {
        val response = database.getFoldersWithCountsSubElementsByLocation(param)
        val result = response.toDomain2()
        return result
    }

    override suspend fun deleteByLocation(param: ElementParam): Int {
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
            idFolder = this.id,
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
                id = it.idFolder,
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
            id = this.idFolder,
            name = this.name,
            description = this.description,
            typeLocation = this.typeLocation,
            idLocation = this.idLocation ?: 0,
            dateCreate = this.dateCreate,
            dateEdit = this.dateEdit,
            dateArchive = this.dateArchive
        )
    }

    @Deprecated("Локация это заглушка", level = DeprecationLevel.WARNING)
    // Жалуется на то, что компилятор не может распознать разные toDomain, потому что не учитывает входной тип
    private fun List<FolderExt>.toDomain2(): List<FolderWithCounters> {
        return this.map {
            FolderWithCounters(
                folder = FolderInfo(
                    id = it.idFolder,
                    name = it.name,
                    description = null,
                    typeLocation = TypeElement.DEFAULT,
                    idLocation = 0,
                    dateCreate = it.dateCreate,
                    dateEdit = it.dateEdit,
                    dateArchive = null
                ),
                countSubFolders = it.countSubFolders,
                countSubProjects = it.countSubProjects,
                countSubTasks = it.countSubTasks
            )
        }
    }
}