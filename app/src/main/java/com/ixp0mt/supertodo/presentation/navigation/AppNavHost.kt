package com.ixp0mt.supertodo.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.folder.FolderScreen
import com.ixp0mt.supertodo.presentation.screen.folder.create.CreateFolderScreen
import com.ixp0mt.supertodo.presentation.screen.folder.edit.EditFolderScreen
import com.ixp0mt.supertodo.presentation.screen.main.MainFolderScreen
import com.ixp0mt.supertodo.presentation.screen.project.ProjectScreen
import com.ixp0mt.supertodo.presentation.screen.project.create.CreateProjectScreen
import com.ixp0mt.supertodo.presentation.screen.project.edit.EditProjectScreen
import com.ixp0mt.supertodo.presentation.screen.task.TaskScreen
import com.ixp0mt.supertodo.presentation.screen.task.create.CreateTaskScreen
import com.ixp0mt.supertodo.presentation.screen.task.edit.EditTaskScreen
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.screen.loading.LoadingScreen
import com.ixp0mt.supertodo.presentation.screen.location.ChangeLocationScreen


@Composable
fun AppNavHost(
    navHostController: NavHostController,
    screenState: ScreenState,
    startDestination: String,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        modifier = Modifier
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding),
        navController = navHostController,
        startDestination = startDestination
    ) {

        composable(Routes.Loading.fullRoute) {
            LoadingScreen(
                onComplete = {
                    navHostController.navigate(Routes.MainFolder.fullRoute) {
                        //launchSingleTop = true
                        popUpTo(Routes.Loading.fullRoute) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.MainFolder.fullRoute) {
            MainFolderScreen(
                screenState = screenState,
                onElementClick = { navigateToElementScreen(navHostController, it.typeElement, it.idElement) }
            )
        }
        composable(Routes.Folder.fullRoute) {
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
            CreateFolderScreen(
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onSuccessSave = {
                    navigateToElementScreen(
                        navHostController = navHostController,
                        typeElement = TypeElement.FOLDER,
                        idElement = it.idElement,
                        clearPreviousRoute = Routes.FolderCreate.fullRoute
                    )
                },
                onBackClick = {
                    navHostController.navigateBack()
                }
            )
        }
        composable(Routes.FolderEdit.fullRoute) {
            EditFolderScreen(
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onBackClick = {
                    navHostController.navigateBack()
                }
            )
        }


        composable(Routes.Project.fullRoute) {
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
            CreateProjectScreen(
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onSuccessSave = {
                    navigateToElementScreen(
                        navHostController = navHostController,
                        typeElement = TypeElement.PROJECT,
                        idElement = it.idElement,
                        clearPreviousRoute = Routes.ProjectCreate.fullRoute
                    )
                },
                onBackClick = { navHostController.navigateBack() }
            )
        }
        composable(Routes.ProjectEdit.fullRoute) {
            EditProjectScreen(
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onBackClick = { navHostController.navigateBack() }
            )
        }
        composable(Routes.Task.fullRoute) {
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
            CreateTaskScreen(
                screenState = screenState,
                snackbarHostState = snackbarHostState,
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
            EditTaskScreen(
                screenState = screenState,
                snackbarHostState = snackbarHostState,
                onBackClick = { navHostController.navigateBack() },
                onLocationClick = { typeLocation, idLocation ->
                    navHostController.navigate(Routes.ChangeLocation(typeLocation, idLocation))
                }
            )
        }

        composable(Routes.ChangeLocation.fullRoute) {
            ChangeLocationScreen(
                screenState = screenState,
                onBackClick = { navHostController.navigateBack() },
                onSaveClick = { locationParam: LocationParam ->
                    if(navHostController.canGoBack) {
                        navHostController.previousBackStackEntry?.savedStateHandle?.set("typeLocation", locationParam.typeLocation)
                        navHostController.previousBackStackEntry?.savedStateHandle?.set("idLocation", locationParam.idLocation)
                        navHostController.popBackStack()
                    }
                },
                onElementClick = {
                    Log.d("ttt","type = ${it.typeLocation} | id = ${it.idLocation}")
                    navHostController.navigate(Routes.ChangeLocation(it.typeLocation, it.idLocation)) {
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
