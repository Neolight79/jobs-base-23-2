package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ru.practicum.android.diploma.ui.viewmodel.JobDetailsViewModel

@Composable
fun JobDetailsScreen(
    navController: NavController,
    viewModel: JobDetailsViewModel
) {
    // Отслеживаем основной объект состояний экрана поиска вакансий
    val vacancyIdState = viewModel.vacancyIdState.collectAsState().value

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TestTitle("Экран\nОПИСАНИЯ ВАКАНСИИ")
            TestButton("Назад") {
                navController.navigateUp()
            }
            TestTitle("Текущий идентификатор:")
            TitleText(vacancyIdState)
        }
    }
}
