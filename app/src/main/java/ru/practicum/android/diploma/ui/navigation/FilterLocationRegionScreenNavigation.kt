package ru.practicum.android.diploma.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.practicum.android.diploma.ui.compoose.ANIMATION_DELAY
import ru.practicum.android.diploma.ui.compoose.FilterLocationRegionScreen
import ru.practicum.android.diploma.ui.compoose.ROUTE_FILTER_LOCATION_REGION

fun NavGraphBuilder.filterLocationRegionScreenNavigation(navController: NavHostController) {
    composable(
        route = ROUTE_FILTER_LOCATION_REGION,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIMATION_DELAY))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIMATION_DELAY))
        }
    ) {
        FilterLocationRegionScreen(navController = navController)
    }
}
