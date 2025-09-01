package ru.practicum.android.diploma.ui.compoose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.BottomBarItem
import ru.practicum.android.diploma.domain.models.BottomNavRoutes
import ru.practicum.android.diploma.ui.navigation.favoritesScreenNavigation
import ru.practicum.android.diploma.ui.navigation.filterIndustryScreenNavigation
import ru.practicum.android.diploma.ui.navigation.filterLocationCountryScreenNavigation
import ru.practicum.android.diploma.ui.navigation.filterLocationRegionScreenNavigation
import ru.practicum.android.diploma.ui.navigation.filterLocationScreenNavigation
import ru.practicum.android.diploma.ui.navigation.filtersScreenNavigation
import ru.practicum.android.diploma.ui.navigation.jobDetailsScreenNavigation
import ru.practicum.android.diploma.ui.navigation.mainScreenNavigation
import ru.practicum.android.diploma.ui.navigation.teamScreenNavigation
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.BottomBarHeight
import ru.practicum.android.diploma.ui.theme.Gray
import ru.practicum.android.diploma.ui.theme.SpacerThin
import ru.practicum.android.diploma.ui.theme.ZeroSize

const val ROUTE_JOB_DETAILS = "jobDetails"
const val ROUTE_FILTERS = "searchFilters"
const val ROUTE_FILTER_LOCATION = "searchFilterLocation"
const val ROUTE_FILTER_LOCATION_COUNTRY = "searchFilterLocationCountry"
const val ROUTE_FILTER_LOCATION_REGION = "searchFilterLocationRegion"
const val ROUTE_FILTER_INDUSTRY = "searchFilterIndustry"

const val JOB_ID = "jobID"

const val ANIMATION_DELAY = 500
const val ZERO_DELAY = 0

@Composable
fun NavScreen() {
    val bottomBarRoutes = listOf(
        BottomBarItem(
            label = stringResource(R.string.screen_main),
            icon = ImageVector.vectorResource(R.drawable.menu_main),
            route = BottomNavRoutes.Main
        ),
        BottomBarItem(
            label = stringResource(R.string.screen_favorites),
            icon = ImageVector.vectorResource(R.drawable.menu_favorites),
            route = BottomNavRoutes.Favorites
        ),
        BottomBarItem(
            label = stringResource(R.string.screen_team),
            icon = ImageVector.vectorResource(R.drawable.menu_team),
            route = BottomNavRoutes.Team
        )
    )

    val navController = rememberNavController()

    val bottomNavRoutes = BottomNavRoutes.entries.map { it.name }
    val showBottomBar = remember { mutableStateOf(true) }
    showBottomBar.value = navController.currentBackStackEntryAsState().value?.destination?.route in bottomNavRoutes
        || navController.currentBackStackEntryAsState().value?.destination?.route.isNullOrEmpty()

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar.value,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = ANIMATION_DELAY)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = ZERO_DELAY)
                )
            ) {
                Column {
                    HorizontalDivider(thickness = SpacerThin)
                    NavigationBar(
                        modifier = Modifier.defaultMinSize(minHeight = BottomBarHeight),
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        bottomBarRoutes.forEach { bottomBarRoute ->
                            NavigationBarItem(
                                modifier = Modifier.defaultMinSize(minHeight = BottomBarHeight),
                                icon = {
                                    Icon(
                                        modifier = Modifier.padding(ZeroSize),
                                        imageVector = bottomBarRoute.icon,
                                        contentDescription = bottomBarRoute.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = bottomBarRoute.label,
                                        modifier = Modifier.padding(ZeroSize),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                },
                                selected = currentDestination?.route == bottomBarRoute.route.name,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Blue,
                                    selectedTextColor = Blue,
                                    unselectedIconColor = Gray,
                                    unselectedTextColor = Gray,
                                    indicatorColor = Color.Transparent
                                ),
                                onClick = {
                                    navController.navigate(bottomBarRoute.route.name) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavRoutes.Main.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Экран ГЛАВНАЯ
            mainScreenNavigation(navController = navController)
            // Экран ИЗБРАННОЕ
            favoritesScreenNavigation(navController = navController)
            // Экран КОМАНДА
            teamScreenNavigation(navController = navController)
            // Экран ФИЛЬТРОВ
            filtersScreenNavigation(navController = navController)
            // Экран ФИЛЬТРА МЕСТА РАБОТЫ
            filterLocationScreenNavigation(navController = navController)
            // Экран ФИЛЬТРА СТРАНЫ
            filterLocationCountryScreenNavigation(navController = navController)
            // Экран ФИЛЬТРА РЕГИОНА
            filterLocationRegionScreenNavigation(navController = navController)
            // Экран ФИЛЬТРА ОТРАСЛИ
            filterIndustryScreenNavigation(navController = navController)
            // Экран ДЕТАЛЕЙ О ВАКАНСИИ
            jobDetailsScreenNavigation(navController = navController)
        }
    }
}
