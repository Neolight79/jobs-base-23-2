package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.FavoritesState
import ru.practicum.android.diploma.domain.models.PlaceholderImages
import ru.practicum.android.diploma.ui.theme.PaddingBase
import ru.practicum.android.diploma.ui.theme.TopBarHeight
import ru.practicum.android.diploma.ui.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = koinViewModel<FavoritesViewModel>()
) {
    // Отслеживаем основной объект состояний экрана избранных вакансий
    val favoritesState = viewModel.favoritesState.collectAsState().value

    // При возвращении на экран загружаем обновленный список избранных вакансий
    LifecycleResumeEffect(Unit) {
        viewModel.loadFavoriteVacancies()
        onPauseOrDispose { }
    }

    // Формируем главную поверхность для макета экрана
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Сверху выводим заголовок экрана избранных вакансий
        FavoritesTopBar()

        // Содержимое главной области определяется на основании полученного состояния
        when (favoritesState) {
            // Во время загрузки результатов выводим прогрессбар
            FavoritesState.Loading -> ProgressbarBox()

            // Если список избранных вакансий пуст, выводим соответствующий плейсхолдер
            FavoritesState.EmptyList -> Placeholder(
                PlaceholderImages.FavoritesEmptyList,
                stringResource(R.string.empty_list)
            )

            // Если список избранных вакансий пуст, выводим соответствующий плейсхолдер
            FavoritesState.ErrorState -> Placeholder(
                PlaceholderImages.EmptyJobList,
                stringResource(R.string.empty_search_result)
            )

            // Состояние вывода списка избранных вакансий
            is FavoritesState.FavoriteVacancies -> {
                VacanciesList(
                    foundVacanciesList = favoritesState.vacanciesList,
                    isShowTrailingPlaceholder = false,
                    onLoadNextPage = { Unit },
                    onItemClick = { vacancy ->
                        navController.navigate("$ROUTE_JOB_DETAILS/${vacancy.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun FavoritesTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth().height(TopBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(PaddingBase)) {
            TitleText(stringResource(R.string.title_favorites))
        }
    }
}
