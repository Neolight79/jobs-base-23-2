package ru.practicum.android.diploma.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.practicum.android.diploma.ui.compoose.ANIMATION_DELAY
import ru.practicum.android.diploma.ui.compoose.FilterLocationCountryScreen
import ru.practicum.android.diploma.ui.compoose.ROUTE_FILTER_LOCATION_COUNTRY

fun NavGraphBuilder.filterLocationCountryScreenNavigation(navController: NavHostController) {
    composable(
        route = ROUTE_FILTER_LOCATION_COUNTRY,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIMATION_DELAY))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIMATION_DELAY))
        }
    ) {
        FilterLocationCountryScreen(navController = navController)
    }
}
