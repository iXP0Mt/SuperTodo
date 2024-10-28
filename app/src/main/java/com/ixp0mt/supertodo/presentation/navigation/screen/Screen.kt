package com.ixp0mt.supertodo.presentation.navigation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.ixp0mt.supertodo.presentation.navigation.Routes
import com.ixp0mt.supertodo.presentation.navigation.top.ActionTopBar
import com.ixp0mt.supertodo.presentation.util.TypeAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface Screen {
    val route: Routes
    val isAppBarVisible: Boolean
    val navigationIcon: ImageVector?
    val navigationIconContentDescription: String?
    val onNavigationIconClick: (() -> Unit)?
    val title: String
    val actionsTopBar: List<ActionTopBar>


    class Loading : Screen {
        override val route: Routes = Routes.Loading
        override val isAppBarVisible: Boolean = false
        override val navigationIcon: ImageVector? = null
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit)? = null
        override val title: String = ""
        override val actionsTopBar: List<ActionTopBar> = emptyList()
    }

    class MainFolder : Screen {
        override val route: Routes = Routes.MainFolder
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector? = null
        override val navigationIconContentDescription: String? = null
        override val title: String = "Главная папка"
        override val onNavigationIconClick: (() -> Unit)? = null
        override val actionsTopBar: List<ActionTopBar> = emptyList()
    }

    class Folder : Screen {
        private var _title by mutableStateOf<String>("Папка")
        fun setTitle(title: String) { _title = title }

        private val _buttons = MutableSharedFlow<TypeAction>(extraBufferCapacity = 1)
        val buttons: Flow<TypeAction> = _buttons.asSharedFlow()


        override val route: Routes = Routes.Folder
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val title: String get() = _title
        override val onNavigationIconClick: (() -> Unit) = { _buttons.tryEmit(TypeAction.ACTION_NAV_BACK) }
        override val actionsTopBar: List<ActionTopBar> = listOf(
            ActionTopBar.EditItem { _buttons.tryEmit(it) },
            ActionTopBar.DeleteItem { _buttons.tryEmit(it) }
        )
    }

    class FolderCreate : Screen {
        private val _buttons = MutableSharedFlow<TypeAction>(extraBufferCapacity = 1)
        val buttons: Flow<TypeAction> = _buttons.asSharedFlow()

        override val route: Routes = Routes.FolderCreate
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit) = { _buttons.tryEmit(TypeAction.ACTION_NAV_BACK) }
        override val title: String = "Создание новой папки"
        override val actionsTopBar: List<ActionTopBar> = listOf(
            ActionTopBar.SaveItem { _buttons.tryEmit(it) }
        )
    }

    class FolderEdit : Screen {
        private val _buttons = MutableSharedFlow<TypeAction>(extraBufferCapacity = 1)
        val buttons: Flow<TypeAction> = _buttons.asSharedFlow()

        override val route: Routes = Routes.FolderEdit
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit) = { _buttons.tryEmit(TypeAction.ACTION_NAV_BACK) }
        override val title: String = "Изменение папки"
        override val actionsTopBar: List<ActionTopBar> = listOf(
            ActionTopBar.SaveItem { _buttons.tryEmit(it) }
        )
    }

    class Project : Screen {
        private val _buttons = MutableSharedFlow<TypeAction>(extraBufferCapacity = 1)
        val buttons: Flow<TypeAction> = _buttons.asSharedFlow()

        override val route: Routes = Routes.Project
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit) = { _buttons.tryEmit(TypeAction.ACTION_NAV_BACK) }
        override val title: String = "Проект"
        override val actionsTopBar: List<ActionTopBar> = listOf(
            ActionTopBar.EditItem { _buttons.tryEmit(it) },
            ActionTopBar.DeleteItem { _buttons.tryEmit(it) }
        )
    }

    class ProjectCreate : Screen {
        private val _buttons = MutableSharedFlow<TypeAction>(extraBufferCapacity = 1)
        val buttons: Flow<TypeAction> = _buttons.asSharedFlow()

        override val route: Routes = Routes.ProjectCreate
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit) = { _buttons.tryEmit(TypeAction.ACTION_NAV_BACK) }
        override val title: String = "Создание проекта"
        override val actionsTopBar: List<ActionTopBar> = listOf(
            ActionTopBar.SaveItem { _buttons.tryEmit(it) }
        )
    }

    class ProjectEdit : Screen {
        private val _buttons = MutableSharedFlow<TypeAction>(extraBufferCapacity = 1)
        val buttons: Flow<TypeAction> = _buttons.asSharedFlow()

        override val route: Routes = Routes.ProjectEdit
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit) = { _buttons.tryEmit(TypeAction.ACTION_NAV_BACK) }
        override val title: String = "Изменение проекта"
        override val actionsTopBar: List<ActionTopBar> = listOf(
            ActionTopBar.SaveItem { _buttons.tryEmit(it) }
        )
    }


    class Task : Screen {
        private val _buttons = MutableSharedFlow<TypeAction>(extraBufferCapacity = 1)
        val buttons: Flow<TypeAction> = _buttons.asSharedFlow()

        override val route: Routes = Routes.Task
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit) = { _buttons.tryEmit(TypeAction.ACTION_NAV_BACK) }
        override val title: String = "Задача"
        override val actionsTopBar: List<ActionTopBar> = listOf(
            ActionTopBar.EditItem { _buttons.tryEmit(it) },
            ActionTopBar.DeleteItem { _buttons.tryEmit(it) }
        )
    }

    class TaskCreate : Screen {
        private val _buttons = MutableSharedFlow<TypeAction>(extraBufferCapacity = 1)
        val buttons: Flow<TypeAction> = _buttons.asSharedFlow()

        override val route: Routes = Routes.TaskCreate
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit) = { _buttons.tryEmit(TypeAction.ACTION_NAV_BACK) }
        override val title: String = "Создание задачи"
        override val actionsTopBar: List<ActionTopBar> = listOf(
            ActionTopBar.SaveItem { _buttons.tryEmit(it) }
        )
    }

    class TaskEdit : Screen {
        private val _buttons = MutableSharedFlow<TypeAction>(extraBufferCapacity = 1)
        val buttons: Flow<TypeAction> = _buttons.asSharedFlow()

        override val route: Routes = Routes.TaskEdit
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit) = { _buttons.tryEmit(TypeAction.ACTION_NAV_BACK) }
        override val title: String = "Изменение задачи"
        override val actionsTopBar: List<ActionTopBar> = listOf(
            ActionTopBar.SaveItem { _buttons.tryEmit(it) }
        )
    }
}


fun getScreen(rawRoute: String?): Screen? {
    return when(rawRoute) {
        Routes.Loading.rawRoute -> Screen.Loading()

        Routes.MainFolder.rawRoute -> Screen.MainFolder()

        Routes.Folder.rawRoute -> Screen.Folder()
        Routes.FolderCreate.rawRoute -> Screen.FolderCreate()
        Routes.FolderEdit.rawRoute -> Screen.FolderEdit()

        Routes.Project.rawRoute -> Screen.Project()
        Routes.ProjectCreate.rawRoute -> Screen.ProjectCreate()
        Routes.ProjectEdit.rawRoute -> Screen.ProjectEdit()

        Routes.Task.rawRoute -> Screen.Task()
        Routes.TaskCreate.rawRoute -> Screen.TaskCreate()
        Routes.TaskEdit.rawRoute -> Screen.TaskEdit()
        else -> null
    }
}