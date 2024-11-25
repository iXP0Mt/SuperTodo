package com.ixp0mt.supertodo.presentation.navigation.bottom

import androidx.annotation.DrawableRes
import com.ixp0mt.supertodo.R
import com.ixp0mt.supertodo.presentation.navigation.Routes


sealed interface ActionBottomBar {
    val label: String?
    @get:DrawableRes val iconId: Int?


    companion object {
        val itemsForMainPanel = listOf(
            Navigation.Main,
            Empty,
            ShowSecondPanel,
            Empty,
            Empty
        )
        val itemsForSecondPanel = listOf(
            Navigation.CreateFolder,
            Navigation.CreateProject,
            ShowMainPanel,
            Navigation.CreateTask,
            Empty
        )
    }


    data object Empty : ActionBottomBar {
        override val label: String? = null
        override val iconId: Int? = null
    }

    data object ShowSecondPanel : ActionBottomBar {
        override val label: String? = null
        override val iconId: Int = R.drawable.ic_add_element
    }

    data object ShowMainPanel : ActionBottomBar {
        override val label: String? = null
        override val iconId: Int = R.drawable.ic_return_bottom_panel
    }


    sealed interface Navigation : ActionBottomBar {
        val route: String?
        val bannedSources: List<String>

        data object Main : Navigation {
            override val route: String = Routes.MainFolder.rawRoute
            override val bannedSources: List<String> = listOf(
                Routes.MainFolder.rawRoute
            )
            override val label: String? = null
            override val iconId: Int = R.drawable.ic_home
        }

        data object CreateFolder : Navigation {
            override val route: String = Routes.FolderCreate.rawRoute
            override val bannedSources: List<String> = listOf(
                Routes.FolderCreate.rawRoute,
                Routes.FolderEdit.rawRoute,
                Routes.ProjectCreate.rawRoute,
                Routes.ProjectEdit.rawRoute,
                Routes.Task.rawRoute,
                Routes.TaskCreate.rawRoute,
                Routes.TaskEdit.rawRoute,
                Routes.ChangeLocation.rawRoute
            )
            override val label: String? = null
            override val iconId: Int = R.drawable.ic_folder
        }

        data object CreateProject : Navigation {
            override val route: String = Routes.ProjectCreate.rawRoute
            override val bannedSources: List<String> = listOf(
                Routes.FolderCreate.rawRoute,
                Routes.FolderEdit.rawRoute,
                Routes.ProjectCreate.rawRoute,
                Routes.ProjectEdit.rawRoute,
                Routes.Task.rawRoute,
                Routes.TaskCreate.rawRoute,
                Routes.TaskEdit.rawRoute,
                Routes.ChangeLocation.rawRoute
            )
            override val label: String? = null
            override val iconId: Int = R.drawable.ic_project
        }

        data object CreateTask : Navigation {
            override val route: String = Routes.TaskCreate.rawRoute
            override val bannedSources: List<String> = listOf(
                Routes.FolderCreate.rawRoute,
                Routes.FolderEdit.rawRoute,
                Routes.ProjectCreate.rawRoute,
                Routes.ProjectEdit.rawRoute,
                Routes.TaskCreate.rawRoute,
                Routes.TaskEdit.rawRoute,
                Routes.ChangeLocation.rawRoute
            )
            override val label: String? = null
            override val iconId: Int = R.drawable.ic_task
        }
    }


    fun isItemEnabled(currentRoute: String?): Boolean {
        val rawRoute = currentRoute?.substringBefore("/")
        when(this) {
            is Navigation -> {
                this.bannedSources.forEach { bannedItem ->
                    if(rawRoute == bannedItem) return false
                }
                return true
            }
            is Empty -> return false
            else -> return true
        }
    }
}
