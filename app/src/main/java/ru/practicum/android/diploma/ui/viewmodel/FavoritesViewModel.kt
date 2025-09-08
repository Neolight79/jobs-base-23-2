package ru.practicum.android.diploma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.FavoritesState
import ru.practicum.android.diploma.domain.models.Vacancy

class FavoritesViewModel(
    private val vacanciesInteractor: VacanciesInteractor
) : ViewModel() {

    // StateFlow для состояния экрана избранных вакансий
    private val _favoritesState = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val favoritesState: StateFlow<FavoritesState> = _favoritesState.asStateFlow()

    // region Публичные методы
    fun loadFavoriteVacancies() {
        _favoritesState.value = FavoritesState.Loading

        viewModelScope.launch {
            // ToDo вызывать функцию получения списка избранных вакансий и флага ошибки, затем передать в обработку результата
//            favoritesInteractor.getFavoriteVacancies().collect { pair ->
//                processResult(pair.first, pair.second)
//            }
        }
    }
    // endregion

    // region Приватные методы
    private fun processResult(favoriteVacancies: List<Vacancy>?, isSuccess: Boolean) {
        if (isSuccess && favoriteVacancies.isNullOrEmpty()) {
            renderState(FavoritesState.EmptyList)
        } else if (!isSuccess) {
            renderState(FavoritesState.ErrorState)
        } else {
            renderState(FavoritesState.FavoriteVacancies(favoriteVacancies.orEmpty()))
        }
    }

    private fun renderState(state: FavoritesState) {
        _favoritesState.value = state
    }
    // endregion

}
