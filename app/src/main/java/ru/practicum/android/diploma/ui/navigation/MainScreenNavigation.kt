package ru.practicum.android.diploma.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.practicum.android.diploma.domain.models.BottomNavRoutes
import ru.practicum.android.diploma.ui.compoose.ANIMATION_DELAY
import ru.practicum.android.diploma.ui.compoose.MainScreen

fun NavGraphBuilder.mainScreenNavigation(navController: NavHostController) {
    composable(
        route = BottomNavRoutes.Main.name,
        enterTransition = {
            if (BottomNavRoutes.isInEnum(initialState.destination.route.toString())) {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIMATION_DELAY))
            } else {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(ANIMATION_DELAY))
            }
        },
        exitTransition = {
            if (BottomNavRoutes.isInEnum(targetState.destination.route.toString())) {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIMATION_DELAY))
            } else {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(ANIMATION_DELAY))
            }
        }
    ) {
        MainScreen(navController = navController)
    }
}
