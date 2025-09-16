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
import ru.practicum.android.diploma.ui.compoose.COUNTRY_ID
import ru.practicum.android.diploma.ui.compoose.FilterLocationRegionScreen
import ru.practicum.android.diploma.ui.compoose.ROUTE_FILTER_LOCATION_REGION
import ru.practicum.android.diploma.ui.viewmodel.FilterLocationRegionViewModel

fun NavGraphBuilder.filterLocationRegionScreenNavigation(navController: NavHostController) {
    composable(
        route = "$ROUTE_FILTER_LOCATION_REGION/{$COUNTRY_ID}",
        arguments = listOf(navArgument(COUNTRY_ID) { type = NavType.IntType }),
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIMATION_DELAY))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIMATION_DELAY))
        }
    ) { backStackEntry ->
        val countryID = backStackEntry.arguments?.getInt(COUNTRY_ID) ?: 0
        FilterLocationRegionScreen(
            navController = navController,
            viewModel = koinViewModel<FilterLocationRegionViewModel>(parameters = { parametersOf(countryID) })
        )
    }
}
