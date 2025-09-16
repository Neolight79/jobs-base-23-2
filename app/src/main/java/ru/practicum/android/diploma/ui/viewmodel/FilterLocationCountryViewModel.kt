package ru.practicum.android.diploma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterAreasInteractor
import ru.practicum.android.diploma.domain.models.CountriesState

class FilterLocationCountryViewModel(
    private val areasInteractor: FilterAreasInteractor
) : ViewModel() {

    // Состояние для экрана списка выбора страны
    private val _countriesState = MutableStateFlow<CountriesState>(CountriesState.Loading)
    val countriesState: StateFlow<CountriesState> = _countriesState.asStateFlow()

    fun loadCountries() {
        viewModelScope.launch {
            _countriesState.value = CountriesState.Loading
            val countriesResponse = areasInteractor.getFilterAreasCountries()
            if (countriesResponse.first.isNullOrEmpty()) {
                _countriesState.value = CountriesState.ServerError
            } else {
                val countries = countriesResponse.first.orEmpty()
                _countriesState.value = CountriesState.Countries(countries = countries)
            }
        }
    }

}
