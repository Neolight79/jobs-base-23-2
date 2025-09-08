package ru.practicum.android.diploma.domain.models

sealed interface FavoritesState {

    data object Loading : FavoritesState

    data object EmptyList : FavoritesState

    data object ErrorState : FavoritesState

    data class FavoriteVacancies(
        val vacanciesList: List<Vacancy>
    ) : FavoritesState

}
