package com.ixp0mt.supertodo.presentation.navigation.screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.ixp0mt.supertodo.presentation.navigation.top.ActionTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Stable
class ScreenState(
    navController: NavController,
    scope: CoroutineScope
) {
    var currentScreen by mutableStateOf<Screen?>(null)
        private set

    var currentArgs by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    var previousRawRoute by mutableStateOf<String?>(null)
        private set

    var previousArgs by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    val isVisible: Boolean
        @Composable get() = currentScreen?.isAppBarVisible == true

    val navigationIcon: ImageVector?
        @Composable get() = currentScreen?.navigationIcon

    val navigationIconContentDescription: String?
        @Composable get() = currentScreen?.navigationIconContentDescription

    val onNavigationIconClick: (() -> Unit)?
        @Composable get() = currentScreen?.onNavigationIconClick

    val title: String
        @Composable get() = currentScreen?.title.orEmpty()

    val actionsTopBar: List<ActionTopBar>
        @Composable get() = currentScreen?.actionsTopBar.orEmpty()


    init {
        navController.currentBackStackEntryFlow
            .distinctUntilChanged()
            .onEach { backStackEntry ->
                val rawRoute = backStackEntry.destination.route?.substringBefore("/")
                currentScreen = getScreen(rawRoute)
                Log.d("ttt","currentSCREEN: $currentScreen")

                val args = backStackEntry.arguments
                currentArgs = args?.keySet()?.associateWith { key ->
                    args.getString(key, "")
                } ?: emptyMap()

                val prevBackStackEntry = navController.previousBackStackEntry
                previousRawRoute = prevBackStackEntry?.destination?.route?.substringBefore("/")
                val prevArgs = prevBackStackEntry?.arguments
                previousArgs = prevArgs?.keySet()?.associateWith {
                    prevArgs.getString(it).orEmpty()
                } ?: emptyMap()
            }
            .launchIn(scope)
    }
}


@Composable
fun rememberScreenState(
    navController: NavController,
    scope: CoroutineScope = rememberCoroutineScope()
) = remember { ScreenState(navController, scope) }