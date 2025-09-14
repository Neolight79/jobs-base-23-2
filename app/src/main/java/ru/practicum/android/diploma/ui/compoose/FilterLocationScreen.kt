package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Location
import ru.practicum.android.diploma.ui.theme.PaddingBase
import ru.practicum.android.diploma.ui.theme.PaddingSmall
import ru.practicum.android.diploma.ui.viewmodel.FilterLocationViewModel

private const val WEIGHT_1F = 1F

@Composable
fun FilterLocationScreen(
    navController: NavController,
    viewModel: FilterLocationViewModel = koinViewModel<FilterLocationViewModel>()
) {
    // Отслеживаем основной объект состояний экрана настройки места работы
    val filterLocationState = viewModel.filterLocationState.collectAsState().value

    // Отслеживаем возврат страны с экрана выбора страны
    val countryResult = navController.currentBackStackEntry?.savedStateHandle
        ?.getLiveData<String>("selectedCountry")?.observeAsState()

    LaunchedEffect(countryResult) {
        countryResult?.value?.let {
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selectedCountry")
            viewModel.updateCountry(Gson().fromJson(it, object : TypeToken<Location>() {}.type))
        }
    }

    // Отслеживаем возврат региона с экрана выбора региона
    val regionResult = navController.currentBackStackEntry?.savedStateHandle
        ?.getLiveData<String>("selectedRegion")?.observeAsState()

    LaunchedEffect(regionResult) {
        regionResult?.value?.let {
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selectedRegion")
            viewModel.updateRegion(Gson().fromJson(it, object : TypeToken<Location>() {}.type))
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Сверху выводим заголовок с кнопкой назад
        FiltersTopBar(stringResource(R.string.select_job_location)) {
            navController.navigateUp()
        }
        Spacer(modifier = Modifier.height(PaddingBase))
        // Далее выводим две строки для выбора страны и региона
        SettingsRow(
            hint = stringResource(R.string.country),
            data = filterLocationState.countryName,
            onClick = {
                navController.navigate(ROUTE_FILTER_LOCATION_COUNTRY)
            },
            onClear = {
                viewModel.clearCountry()
            }
        )
        SettingsRow(
            hint = stringResource(R.string.region),
            data = filterLocationState.region,
            onClick = {
                if (filterLocationState.countryId != null) {
                    navController.navigate("$ROUTE_FILTER_LOCATION_REGION/${filterLocationState.countryId}")
                } else {
                    navController.navigate("$ROUTE_FILTER_LOCATION_REGION/0")
                }
            },
            onClear = {
                viewModel.clearRegion()
            }
        )
        Spacer(modifier = Modifier.weight(WEIGHT_1F))
        if (filterLocationState.isDataSelected) {
            // Далее выводим кнопку применения выбранной локации
            ConfirmButton(stringResource(R.string.approve)) {
                viewModel.saveState()
                navController.navigateUp()
            }
        }
        Spacer(modifier = Modifier.height(PaddingBase))
        Spacer(modifier = Modifier.height(PaddingSmall))
    }
}
