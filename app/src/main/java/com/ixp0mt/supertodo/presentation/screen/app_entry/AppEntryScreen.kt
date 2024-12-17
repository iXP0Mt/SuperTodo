package com.ixp0mt.supertodo.presentation.screen.app_entry

import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.ixp0mt.supertodo.presentation.component.SnackBar
import com.ixp0mt.supertodo.presentation.navigation.AppNavHost
import com.ixp0mt.supertodo.presentation.navigation.Routes
import com.ixp0mt.supertodo.presentation.navigation.bottom.CustomBottomBar
import com.ixp0mt.supertodo.presentation.navigation.bottom.CustomBottomBarViewModel
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.navigation.top.TopBar

@Composable
fun AppEntryScreen(
    screenState: ScreenState,
    bottomBarViewModel: CustomBottomBarViewModel,
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val startDestination: String = Routes.MainFolder.rawRoute

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
                    CustomBottomBar(
                        viewModel = bottomBarViewModel,
                        currentRoute = screenState.currentScreen.route.fullRoute,
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
