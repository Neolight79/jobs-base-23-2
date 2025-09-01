package ru.practicum.android.diploma.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.practicum.android.diploma.ui.compoose.ANIMATION_DELAY
import ru.practicum.android.diploma.ui.compoose.FilterLocationScreen
import ru.practicum.android.diploma.ui.compoose.ROUTE_FILTERS
import ru.practicum.android.diploma.ui.compoose.ROUTE_FILTER_LOCATION

fun NavGraphBuilder.filterLocationScreenNavigation(navController: NavHostController) {
    composable(
        route = ROUTE_FILTER_LOCATION,
        enterTransition = {
            if (initialState.destination.route == ROUTE_FILTERS) {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIMATION_DELAY))
            } else {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIMATION_DELAY))
            }
        },
        exitTransition = {
            if (targetState.destination.route == ROUTE_FILTERS) {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIMATION_DELAY))
            } else {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIMATION_DELAY))
            }
        }
    ) {
        FilterLocationScreen(navController = navController)
    }
}
