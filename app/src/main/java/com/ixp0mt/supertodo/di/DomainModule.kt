package com.ixp0mt.supertodo.di

import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.usecase.element.CreateFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.CreateProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.CreateTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetListNamesPedigreeUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetLocationAsElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetMainFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetSubElementsUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetSubElementsWithCountersUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.element.MarkElementCompleteUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveEditFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveEditProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveEditTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveNewFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveNewProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveNewTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.element.ValidFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.ValidProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.ValidTaskUseCase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideDeleteElementUseCase(
        folderRepository: FolderRepository,
        projectRepository: ProjectRepository,
        taskRepository: TaskRepository
    ): DeleteElementUseCase {
        return DeleteElementUseCase(
            folderRepository,
            projectRepository,
            taskRepository
        )
    }

    @Provides
    fun provideGetMainFolderUseCase(): GetMainFolderUseCase {
        return GetMainFolderUseCase()
    }
    
    @Provides
    fun provideGetFolderUseCase(folderRepository: FolderRepository): GetFolderUseCase {
        return GetFolderUseCase(folderRepository)
    }

    @Provides
    fun provideGetProjectUseCase(projectRepository: ProjectRepository): GetProjectUseCase {
        return GetProjectUseCase(projectRepository)
    }

    @Provides
    fun provideGetTaskUseCase(taskRepository: TaskRepository): GetTaskUseCase {
        return GetTaskUseCase(taskRepository)
    }

    @Provides
    fun provideGetListNamesPedigreeUseCase(
        folderRepository: FolderRepository,
        projectRepository: ProjectRepository,
        taskRepository: TaskRepository
    ): GetListNamesPedigreeUseCase {
        return GetListNamesPedigreeUseCase(
            folderRepository,
            projectRepository,
            taskRepository
        )
    }

    @Provides
    fun provideGetLocationAsElementUseCase(
        folderRepository: FolderRepository,
        projectRepository: ProjectRepository,
        taskRepository: TaskRepository
    ): GetLocationAsElementUseCase {
        return GetLocationAsElementUseCase(
            folderRepository,
            projectRepository,
            taskRepository
        )
    }

    @Provides
    fun provideGetSubElementsUseCase(
        folderRepository: FolderRepository,
        projectRepository: ProjectRepository,
        taskRepository: TaskRepository
    ): GetSubElementsUseCase {
        return GetSubElementsUseCase(
            folderRepository,
            projectRepository,
            taskRepository
        )
    }

    @Provides
    fun provideGetSubElementsWithCountersUseCase(
        folderRepository: FolderRepository,
        projectRepository: ProjectRepository,
        taskRepository: TaskRepository
    ): GetSubElementsWithCountersUseCase {
        return GetSubElementsWithCountersUseCase(
            folderRepository,
            projectRepository,
            taskRepository
        )
    }

    @Provides
    fun provideMarkElementCompleteUseCase(
        projectRepository: ProjectRepository,
        taskRepository: TaskRepository
    ): MarkElementCompleteUseCase {
        return MarkElementCompleteUseCase(
            projectRepository,
            taskRepository
        )
    }

    @Provides
    fun provideSaveNewFolderUseCase(folderRepository: FolderRepository): SaveNewFolderUseCase {
        return SaveNewFolderUseCase(folderRepository)
    }

    @Provides
    fun provideSaveEditFolderUseCase(folderRepository: FolderRepository): SaveEditFolderUseCase {
        return SaveEditFolderUseCase(folderRepository)
    }

    @Provides
    fun provideSaveNewProjectUseCase(projectRepository: ProjectRepository): SaveNewProjectUseCase {
        return SaveNewProjectUseCase(projectRepository)
    }

    @Provides
    fun provideSaveEditProjectUseCase(projectRepository: ProjectRepository): SaveEditProjectUseCase {
        return SaveEditProjectUseCase(projectRepository)
    }

    @Provides
    fun provideSaveNewTaskUseCase(taskRepository: TaskRepository): SaveNewTaskUseCase {
        return SaveNewTaskUseCase(taskRepository)
    }

    @Provides
    fun provideSaveEditTaskUseCase(taskRepository: TaskRepository): SaveEditTaskUseCase {
        return SaveEditTaskUseCase(taskRepository)
    }

    @Provides
    fun provideValidFolderUseCase(): ValidFolderUseCase {
        return ValidFolderUseCase()
    }

    @Provides
    fun provideValidProjectUseCase(): ValidProjectUseCase {
        return ValidProjectUseCase()
    }

    @Provides
    fun provideValidTaskUseCase(): ValidTaskUseCase {
        return ValidTaskUseCase()
    }

    @Provides
    fun provideCreateFolderUseCase(): CreateFolderUseCase {
        return CreateFolderUseCase()
    }

    @Provides
    fun provideCreateProjectUseCase(): CreateProjectUseCase {
        return CreateProjectUseCase()
    }

    @Provides
    fun provideCCreateTaskUseCase(): CreateTaskUseCase {
        return CreateTaskUseCase()
    }
}