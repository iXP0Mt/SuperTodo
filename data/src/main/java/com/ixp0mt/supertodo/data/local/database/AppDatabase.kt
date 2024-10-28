package com.ixp0mt.supertodo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ixp0mt.supertodo.data.local.database.dao.FolderDao
import com.ixp0mt.supertodo.data.local.database.dao.ProjectDao
import com.ixp0mt.supertodo.data.local.database.dao.TaskDao
import com.ixp0mt.supertodo.data.local.database.entity.Folder
import com.ixp0mt.supertodo.data.local.database.entity.Project
import com.ixp0mt.supertodo.data.local.database.entity.Task

@Database(entities = [Folder::class, Project::class, Task::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
}