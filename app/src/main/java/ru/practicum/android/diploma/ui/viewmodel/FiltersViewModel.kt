package ru.practicum.android.diploma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.FiltersState

class FiltersViewModel : ViewModel() {

    // Моковый объект для эмуляции работы с интерактором
    private var stateFromSharedPrefs = FiltersState(
        areFiltersSet = true,
        location = "Россия, Москва",
        industry = "Деревообработка",
        salary = "",
        doNotShowWithoutSalary = false
    )

    // StateFlow для состояния экрана настройки фильтров
    private val _filtersState = MutableStateFlow<FiltersState>(stateFromSharedPrefs)
    val filtersState: StateFlow<FiltersState> = _filtersState.asStateFlow()

    // StateFlow для сокрытия клавиатуры
    private val _hideKeyboardState = MutableSharedFlow<Unit>(replay = 0)
    val hideKeyboardState = _hideKeyboardState.asSharedFlow()

    // region Публичные методы
    fun loadFilters() {
        renderState(stateFromSharedPrefs)

//        viewModelScope.launch {
//            favoritesInteractor.getFavoriteVacancies().collect { vacancies ->
//                processResult(vacancies, true)
//            }
//        }
    }

    fun hideKeyboard() {
        viewModelScope.launch {
            _hideKeyboardState.emit(Unit)
        }
    }

    fun clearLocation() {
        // ToDo Очищаем запись о месте работы в фильтрах в хранилище
        stateFromSharedPrefs = stateFromSharedPrefs.copy(
            location = "",
            areFiltersSet = stateFromSharedPrefs.doNotShowWithoutSalary
                || stateFromSharedPrefs.industry.isNotEmpty()
                || stateFromSharedPrefs.salary.isNotEmpty()
        )
        loadFilters()
    }

    fun clearIndustry() {
        // ToDo Очищаем запись об отрасли в фильтрах в хранилище
        stateFromSharedPrefs = stateFromSharedPrefs.copy(
            industry = "",
            areFiltersSet = stateFromSharedPrefs.doNotShowWithoutSalary
                || stateFromSharedPrefs.location.isNotEmpty()
                || stateFromSharedPrefs.salary.isNotEmpty()
        )
        loadFilters()
    }

    fun toggleDoNotShowWithoutSalary(isChecked: Boolean) {
        // ToDo Переключаем флаг показа только вакансий с зарплатами
        stateFromSharedPrefs = stateFromSharedPrefs.copy(
            doNotShowWithoutSalary = isChecked,
            areFiltersSet = isChecked
                || stateFromSharedPrefs.location.isNotEmpty()
                || stateFromSharedPrefs.industry.isNotEmpty()
                || stateFromSharedPrefs.salary.isNotEmpty()
        )
        loadFilters()
    }

    fun resetFilters() {
        // ToDo Сбрасываем все фильтры
        stateFromSharedPrefs = FiltersState(
            areFiltersSet = false,
            location = "",
            industry = "",
            salary = "",
            doNotShowWithoutSalary = false
        )
        loadFilters()
    }

    fun saveSalary(salaryText: String) {
        // ToDo Сохраняем новое значение желаемой зарплаты для фильтрации
        stateFromSharedPrefs = stateFromSharedPrefs.copy(
            salary = salaryText,
            areFiltersSet = stateFromSharedPrefs.doNotShowWithoutSalary
                || stateFromSharedPrefs.location.isNotEmpty()
                || stateFromSharedPrefs.industry.isNotEmpty()
                || salaryText.isNotEmpty()
        )
        loadFilters()
    }
    // endregion

    // region Приватные методы
//    private fun processResult(favoriteVacancies: List<Vacancy>?, isSuccess: Boolean) {
//        if (isSuccess && favoriteVacancies.isNullOrEmpty()) {
//            renderState(FavoritesState.EmptyList)
//        } else if (!isSuccess) {
//            renderState(FavoritesState.ErrorState)
//        } else {
//            renderState(FavoritesState.FavoriteVacancies(favoriteVacancies.orEmpty()))
//        }
//    }

    private fun renderState(state: FiltersState) {
        _filtersState.value = state
    }
    // endregion

}
