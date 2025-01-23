package com.ixp0mt.supertodo.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.folder.FolderScreen
import com.ixp0mt.supertodo.presentation.screen.main.MainFolderScreen
import com.ixp0mt.supertodo.presentation.screen.project.ProjectScreen
import com.ixp0mt.supertodo.presentation.screen.task.TaskScreen
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.screen.location.ChangeLocationScreen
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EditorEditFolderViewModel
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EditorEditProjectViewModel
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EditorEditTaskViewModel
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EditorElementScreen
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EditorNewFolderViewModel
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EditorNewProjectViewModel
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EditorNewTaskViewModel
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EditorPlanElementScreen


@Composable
fun AppNavHost(
    navHostController: NavHostController,
    screenState: ScreenState,
    startDestination: String,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    val currentScreen = screenState.currentScreen

    NavHost(
        modifier = Modifier
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding),
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(Routes.MainFolder.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            MainFolderScreen(
                screenState = screenState,
                onElementClick = { navigateToElementScreen(navHostController, it.typeElement, it.idElement) }
            )
        }
        composable(Routes.Folder.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            FolderScreen(
                screenState = screenState,
                onBackClick = { navHostController.navigateBack() },
                onElementClick = { navigateToElementScreen(navHostController, it.typeElement, it.idElement) },
                onEditClick = {
                    val idFolder = screenState.currentArgs[Routes.Folder.ID]?.toLongOrNull() ?: 0
                    navHostController.navigate(Routes.FolderEdit(idFolder))
                }
            )
        }
        composable(Routes.FolderCreate.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            val viewModel: EditorNewFolderViewModel = hiltViewModel()
            EditorElementScreen(
                viewModel = viewModel,
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onClickChangeLocation = { navHostController.navigate(Routes.ChangeLocation(it.typeElement, it.idElement)) },
                onBackClick = { navHostController.navigateBack() },
                onSuccessSave = {
                    navigateToElementScreen(
                        navHostController = navHostController,
                        typeElement = TypeElement.FOLDER,
                        idElement = it.idElement,
                        clearPreviousRoute = Routes.FolderCreate.fullRoute
                    )
                },
            )
        }
        composable(Routes.FolderEdit.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            val viewModel: EditorEditFolderViewModel = hiltViewModel()
            EditorElementScreen(
                viewModel = viewModel,
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onClickChangeLocation = { navHostController.navigate(Routes.ChangeLocation(it.typeElement, it.idElement)) },
                onBackClick = { navHostController.navigateBack() },
                onSuccessSave = { navHostController.navigateBack() }
            )
        }


        composable(Routes.Project.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            ProjectScreen(
                screenState = screenState,
                onBackClick = {
                    navHostController.navigateBack()
                },
                onElementClick = { navigateToElementScreen(navHostController, it.typeElement, it.idElement) },
                onEditClick = {
                    val idProject = screenState.currentArgs[Routes.Project.ID]?.toLongOrNull() ?: 0
                    navHostController.navigate(Routes.ProjectEdit(idProject))
                },
            )
        }
        composable(Routes.ProjectCreate.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            val viewModel: EditorNewProjectViewModel = hiltViewModel()
            EditorPlanElementScreen(
                viewModel = viewModel,
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onClickChangeLocation = { navHostController.navigate(Routes.ChangeLocation(it.typeElement, it.idElement)) },
                onBackClick = { navHostController.navigateBack() },
                onSuccessSave = {
                    navigateToElementScreen(
                        navHostController = navHostController,
                        typeElement = TypeElement.PROJECT,
                        idElement = it.idElement,
                        clearPreviousRoute = Routes.ProjectCreate.fullRoute
                    )
                },
            )
        }
        composable(Routes.ProjectEdit.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            val viewModel: EditorEditProjectViewModel = hiltViewModel()
            EditorPlanElementScreen(
                viewModel = viewModel,
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onClickChangeLocation = { navHostController.navigate(Routes.ChangeLocation(it.typeElement, it.idElement)) },
                onBackClick = { navHostController.navigateBack() },
                onSuccessSave = { navHostController.navigateBack() }
            )
        }
        composable(Routes.Task.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            TaskScreen(
                screenState = screenState,
                onBackClick = { navHostController.navigateBack() },
                onEditClick = {
                    val idTask = screenState.currentArgs[Routes.Task.ID]?.toLongOrNull() ?: 0
                    navHostController.navigate(Routes.TaskEdit(idTask))
                },
                onElementClick = { navigateToElementScreen(navHostController, it.typeElement, it.idElement) }
            )
        }
        composable(Routes.TaskCreate.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            val viewModel: EditorNewTaskViewModel = hiltViewModel()
            EditorPlanElementScreen(
                viewModel = viewModel,
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onClickChangeLocation = { navHostController.navigate(Routes.ChangeLocation(it.typeElement, it.idElement)) },
                onBackClick = { navHostController.navigateBack() },
                onSuccessSave = {
                    navigateToElementScreen(
                        navHostController = navHostController,
                        typeElement = TypeElement.TASK,
                        idElement = it.idElement,
                        clearPreviousRoute = Routes.TaskCreate.fullRoute
                    )
                }
            )
        }
        composable(Routes.TaskEdit.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            val viewModel: EditorEditTaskViewModel = hiltViewModel()
            EditorPlanElementScreen(
                viewModel = viewModel,
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onClickChangeLocation = { navHostController.navigate(Routes.ChangeLocation(it.typeElement, it.idElement)) },
                onBackClick = { navHostController.navigateBack() },
                onSuccessSave = { navHostController.navigateBack() }
            )
        }

        composable(Routes.ChangeLocation.fullRoute) {
            if(currentScreen.route == Routes.Default) return@composable

            ChangeLocationScreen(
                screenState = screenState,
                onBackClick = { navHostController.navigateBack() },
                onSaveClick = { locationParam: ElementParam ->
                    if(navHostController.canGoBack) {
                        navHostController.previousBackStackEntry?.savedStateHandle?.set("typeLocation", locationParam.typeElement)
                        navHostController.previousBackStackEntry?.savedStateHandle?.set("idLocation", locationParam.idElement)
                        navHostController.popBackStack()
                    }
                },
                onElementClick = {
                    navHostController.navigate(Routes.ChangeLocation(it.typeElement, it.idElement)) {
                        popUpTo(Routes.ChangeLocation.fullRoute) { inclusive = true }
                    }
                },
            )
        }
    }
}


private fun navigateToElementScreen(
    navHostController: NavHostController,
    typeElement: TypeElement,
    idElement: Long,
    clearPreviousRoute: String? = null
) {
    val route = when (typeElement) {
        TypeElement.FOLDER -> Routes.Folder(idElement)
        TypeElement.PROJECT -> Routes.Project(idElement)
        TypeElement.TASK -> Routes.Task(idElement)
        else -> return
    }
    navHostController.navigate(route) {
        //launchSingleTop = true
        clearPreviousRoute?.let { popUpTo(it) { inclusive = true } }
    }
}

private val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

private fun NavHostController.navigateBack() {
    if (canGoBack)
        popBackStack()
}
