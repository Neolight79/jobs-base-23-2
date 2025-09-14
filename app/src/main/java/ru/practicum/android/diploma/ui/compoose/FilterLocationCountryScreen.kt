package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import com.google.gson.Gson
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.CountriesState
import ru.practicum.android.diploma.domain.models.Location
import ru.practicum.android.diploma.domain.models.PlaceholderImages
import ru.practicum.android.diploma.ui.theme.ArrowBackHeight
import ru.practicum.android.diploma.ui.theme.PaddingBase
import ru.practicum.android.diploma.ui.theme.SettingsRowHeight
import ru.practicum.android.diploma.ui.viewmodel.FilterLocationCountryViewModel

private const val WEIGHT_1F = 1F

@Composable
fun FilterLocationCountryScreen(
    navController: NavController,
    viewModel: FilterLocationCountryViewModel = koinViewModel<FilterLocationCountryViewModel>()
) {
    // Отслеживаем основной объект состояний экрана настройки места работы
    val countriesState = viewModel.countriesState.collectAsState().value

    // При попадании на экран запускаем загрузку стран
    LifecycleResumeEffect(Unit) {
        viewModel.loadCountries()
        onPauseOrDispose { }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Сверху выводим заголовок с кнопкой назад
        FiltersTopBar(stringResource(R.string.country_selection)) {
            navController.navigateUp()
        }
        Spacer(modifier = Modifier.height(PaddingBase))
        // Дальнейшее содержимое зависит от текущего состояния
        when (countriesState) {
            is CountriesState.Loading -> ProgressbarBox()
            is CountriesState.ServerError -> Placeholder(
                PlaceholderImages.JobDetailsServerError,
                stringResource(R.string.server_error)
            )
            is CountriesState.Countries -> LocationsList(
                foundLocationsList = countriesState.countries,
                onItemClick = { country ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedCountry", Gson().toJson(country))
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun LocationsList(
    foundLocationsList: List<Location>,
    onItemClick: (country: Location) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        Modifier.fillMaxSize(),
        state = listState
    ) {
        items(foundLocationsList) { country ->
            CountryListItem(country.name) {
                onItemClick(country)
            }
        }
    }
}

@Composable
fun CountryListItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SettingsRowHeight)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = PaddingBase)
                .weight(WEIGHT_1F)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(SettingsRowHeight),
            contentAlignment = Alignment.Center
        ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_forward),
                    contentDescription = title,
                    modifier = Modifier.size(ArrowBackHeight)
                )
        }
    }
}
