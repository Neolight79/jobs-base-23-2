package ru.practicum.android.diploma.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.practicum.android.diploma.ui.compoose.ANIMATION_DELAY
import ru.practicum.android.diploma.ui.compoose.JOB_ID
import ru.practicum.android.diploma.ui.compoose.JobDetailsScreen
import ru.practicum.android.diploma.ui.compoose.ROUTE_JOB_DETAILS

fun NavGraphBuilder.jobDetailsScreenNavigation(navController: NavHostController) {
    composable(
        route = "$ROUTE_JOB_DETAILS/{$JOB_ID}",
        arguments = listOf(navArgument(JOB_ID) { type = NavType.IntType }),
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(ANIMATION_DELAY))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(ANIMATION_DELAY))
        }
    ) {
        JobDetailsScreen(navController = navController)
    }
}
