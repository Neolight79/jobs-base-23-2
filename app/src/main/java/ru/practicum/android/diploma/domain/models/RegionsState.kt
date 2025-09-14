package ru.practicum.android.diploma.domain.models

sealed interface RegionsState {

    data object Loading : RegionsState

    data object ServerError : RegionsState

    data object EmptyResult : RegionsState

    data class Regions(val regions: List<Location>) : RegionsState

}
