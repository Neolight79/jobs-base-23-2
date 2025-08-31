package ru.practicum.android.diploma.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.practicum.android.diploma.domain.models.BottomNavRoutes
import ru.practicum.android.diploma.ui.compoose.ANIMATION_DELAY
import ru.practicum.android.diploma.ui.compoose.FavoritesScreen

fun NavGraphBuilder.favoritesScreenNavigation(navController: NavHostController) {
    composable(
        route = BottomNavRoutes.Favorites.name,
        enterTransition = {
            when (initialState.destination.route) {
                BottomNavRoutes.Main.name -> {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIMATION_DELAY))
                }
                BottomNavRoutes.Team.name -> {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIMATION_DELAY))
                }
                else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(ANIMATION_DELAY))
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                BottomNavRoutes.Main.name -> {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIMATION_DELAY))
                }
                BottomNavRoutes.Team.name -> {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIMATION_DELAY))
                }
                else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(ANIMATION_DELAY))
            }
        }
    ) {
        FavoritesScreen(navController = navController)
    }
}
