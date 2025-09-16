package ru.practicum.android.diploma.domain.models

sealed interface IndustriesState {

    data object Loading : IndustriesState

    data object ServerError : IndustriesState

    data object EmptyResult : IndustriesState

    data class Industries(
        val industries: List<FilterIndustry>,
        val selectedItem: FilterIndustry?
    ) : IndustriesState

}
