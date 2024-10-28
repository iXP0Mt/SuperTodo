package com.ixp0mt.supertodo.di

import android.content.Context
import com.ixp0mt.supertodo.data.local.database.Database
import com.ixp0mt.supertodo.data.local.database.DatabaseImpl
import com.ixp0mt.supertodo.data.repository.FolderRepositoryImpl
import com.ixp0mt.supertodo.data.repository.ProjectRepositoryImpl
import com.ixp0mt.supertodo.data.repository.TaskRepositoryImpl
import com.ixp0mt.supertodo.domain.repository.FolderRepository
import com.ixp0mt.supertodo.domain.repository.ProjectRepository
import com.ixp0mt.supertodo.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideFolderRepository(database: Database): FolderRepository {
        return FolderRepositoryImpl(database = database)
    }

    @Provides
    @Singleton
    fun provideProjectRepository(database: Database): ProjectRepository {
        return ProjectRepositoryImpl(database = database)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(database: Database): TaskRepository {
        return TaskRepositoryImpl(database = database)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : Database {
        return DatabaseImpl(context = context)
    }

}