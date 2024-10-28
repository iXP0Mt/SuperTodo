package com.ixp0mt.supertodo.presentation.screen.app_entry

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.ixp0mt.supertodo.presentation.component.SnackBar
import com.ixp0mt.supertodo.presentation.navigation.AppNavHost
import com.ixp0mt.supertodo.presentation.navigation.Routes
import com.ixp0mt.supertodo.presentation.navigation.bottom.BottomBar
import com.ixp0mt.supertodo.presentation.navigation.bottom.BottomBarViewModel
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.navigation.top.TopBar

@Composable
fun AppEntryScreen(
    viewModel: AppEntryViewModel,
    screenState: ScreenState,
    bottomBarViewModel: BottomBarViewModel,
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    //val startDestination: String = Routes.MainFolder.rawRoute
    val startDestination: String = Routes.Loading.rawRoute


    Scaffold(
        snackbarHost = { SnackBar(snackbarHostState) },
        topBar = {
            if(screenState.isVisible) {
                TopBar(
                    screenState = screenState,
                )
            }
        },
        bottomBar = {
                BottomBar(
                    viewModel = bottomBarViewModel,
                    currentRoute = screenState.currentScreen?.route?.fullRoute,
                    onNavigationClick = { route ->
                        navHostController.navigate(route) {
                            launchSingleTop = true
                            popUpTo(route) {
                                inclusive = true
                            }
                        }
                    }
                )
        },
        content = { paddingValues ->
            AppNavHost(
                navHostController = navHostController,
                screenState = screenState,
                startDestination = startDestination,
                innerPadding = paddingValues,
                snackbarHostState = snackbarHostState
            )
        }
    )
}
