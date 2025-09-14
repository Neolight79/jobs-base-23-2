package ru.practicum.android.diploma.domain.models

sealed interface CountriesState {

    data object Loading : CountriesState

    data object ServerError : CountriesState

    data class Countries(val countries: List<Location>) : CountriesState

}
