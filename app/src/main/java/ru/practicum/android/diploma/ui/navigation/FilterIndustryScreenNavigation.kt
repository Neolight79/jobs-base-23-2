package ru.practicum.android.diploma.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.practicum.android.diploma.ui.compoose.ANIMATION_DELAY
import ru.practicum.android.diploma.ui.compoose.FilterIndustryScreen
import ru.practicum.android.diploma.ui.compoose.ROUTE_FILTER_INDUSTRY

fun NavGraphBuilder.filterIndustryScreenNavigation(navController: NavHostController) {
    composable(
        route = ROUTE_FILTER_INDUSTRY,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIMATION_DELAY))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIMATION_DELAY))
        }
    ) {
        FilterIndustryScreen(navController = navController)
    }
}
