package ru.practicum.android.diploma.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.ui.compoose.ANIMATION_DELAY
import ru.practicum.android.diploma.ui.compoose.JOB_ID
import ru.practicum.android.diploma.ui.compoose.JobDetailsScreen
import ru.practicum.android.diploma.ui.compoose.ROUTE_JOB_DETAILS
import ru.practicum.android.diploma.ui.viewmodel.JobDetailsViewModel

fun NavGraphBuilder.jobDetailsScreenNavigation(navController: NavHostController) {
    composable(
        route = "$ROUTE_JOB_DETAILS/{$JOB_ID}",
        arguments = listOf(navArgument(JOB_ID) { type = NavType.StringType }),
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(ANIMATION_DELAY))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(ANIMATION_DELAY))
        }
    ) { backStackEntry ->
        val jobID = backStackEntry.arguments?.getString(JOB_ID) ?: 0
        JobDetailsScreen(
            navController = navController,
            viewModel = koinViewModel<JobDetailsViewModel>(parameters = { parametersOf(jobID) })
        )
    }
}
