package com.ixp0mt.supertodo.domain.usecase.folder

import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.model.GetFoldersByTypeLocationParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.util.TypeLocation
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock


class GetFolderByIdUseCaseTest {

    private val folderRepository = mock<FolderRepository>()

    private val testFolder = FolderInfo(
        idFolder = 30,
        name = "testName",
        description = "testDesc",
        typeLocation = TypeLocation.MAIN,
        idLocation = 54,
        dateCreate = 2000
    )

    @AfterEach
    fun tearDown() {
        Mockito.reset(folderRepository)
    }


    @Test
    fun `should call Result failure`() = runBlocking{

        val param = GetFolderByIdParam(idFolder = 1)

        Mockito.`when`(folderRepository.getById(param)).thenThrow(RuntimeException())

        val useCase = GetFolderByIdUseCase(folderRepository)
        val result = useCase.execute(param)

        Assertions.assertTrue(result.isFailure)
    }

    @Test
    fun `must return successful result`() = runBlocking {

        val idFolder = testFolder.idFolder
        val param = GetFolderByIdParam(idFolder)

        Mockito.`when`(folderRepository.getById(param)).thenReturn(testFolder)

        val useCase = GetFolderByIdUseCase(folderRepository)
        val actual = useCase.execute(param).getOrNull()
        val expected = testFolder

        Assertions.assertEquals(expected, actual)
    }
}