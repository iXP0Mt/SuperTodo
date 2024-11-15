package com.ixp0mt.supertodo.di

import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFolderByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.SaveEditFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.project.SaveEditProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.task.SaveEditTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.SaveNewFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.project.SaveNewProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.project.TurnCompleteProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.task.SaveNewTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.task.TurnCompleteTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideSaveNewFolderUseCase(folderRepository: FolderRepository): SaveNewFolderUseCase {
        return SaveNewFolderUseCase(folderRepository = folderRepository)
    }

    @Provides
    fun provideGetFolderById(folderRepository: FolderRepository): GetFolderByIdUseCase {
        return GetFolderByIdUseCase(folderRepository)
    }

    @Provides
    fun provideGetFoldersByLocation(folderRepository: FolderRepository): GetFoldersByLocationUseCase {
        return GetFoldersByLocationUseCase(folderRepository)
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
    fun provideGetProjectsByLocationUseCase(projectRepository: ProjectRepository): GetProjectsByLocationUseCase {
        return GetProjectsByLocationUseCase(projectRepository)
    }

    @Provides
    fun provideGetProjectByIdUseCase(projectRepository: ProjectRepository): GetProjectByIdUseCase {
        return GetProjectByIdUseCase(projectRepository)
    }


    @Provides
    fun provideGetTaskByIdUseCase(taskRepository: TaskRepository): GetTaskByIdUseCase {
        return GetTaskByIdUseCase(taskRepository)
    }

    @Provides
    fun provideGetTasksByLocationUseCase(taskRepository: TaskRepository): GetTasksByLocationUseCase {
        return GetTasksByLocationUseCase(taskRepository)
    }

    @Provides
    fun provideSaveEditTaskUseCase(taskRepository: TaskRepository): SaveEditTaskUseCase {
        return SaveEditTaskUseCase(taskRepository)
    }

    @Provides
    fun provideSaveNewTaskUseCase(taskRepository: TaskRepository): SaveNewTaskUseCase {
        return SaveNewTaskUseCase(taskRepository)
    }


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
    fun provideTurnCompleteTaskUseCase(taskRepository: TaskRepository): TurnCompleteTaskUseCase {
        return TurnCompleteTaskUseCase(taskRepository)
    }

    @Provides
    fun provideTurnCompleteProjectUseCase(projectRepository: ProjectRepository): TurnCompleteProjectUseCase {
        return TurnCompleteProjectUseCase(projectRepository)
    }
}