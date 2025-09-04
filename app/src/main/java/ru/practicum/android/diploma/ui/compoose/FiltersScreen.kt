package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun FiltersScreen(
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TestTitle("Экран\nФИЛЬТРОВ")
            TestButton("Назад") {
                navController.navigateUp()
            }
            TestButton("Фильтр места работы") {
                navController.navigate(ROUTE_FILTER_LOCATION)
            }
            TestButton("Фильтр отрасли") {
                navController.navigate(ROUTE_FILTER_INDUSTRY)
            }
        }
    }
}
