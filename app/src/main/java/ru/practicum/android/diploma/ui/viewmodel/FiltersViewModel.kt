package ru.practicum.android.diploma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.domain.models.FiltersState

class FiltersViewModel(
    private val filtersInteractor: FilterParametersInteractor
) : ViewModel() {

    // Переменная текущих настроек
    private var currentFilters = filtersInteractor.getFilterParameters()

    // StateFlow для состояния экрана настройки фильтров
    private val _filtersState = MutableStateFlow<FiltersState>(getFiltersState())
    val filtersState: StateFlow<FiltersState> = _filtersState.asStateFlow()

    // StateFlow для сокрытия клавиатуры
    private val _hideKeyboardState = MutableSharedFlow<Unit>(replay = 0)
    val hideKeyboardState = _hideKeyboardState.asSharedFlow()

    // region Публичные методы
    fun loadFilters() {
        currentFilters = filtersInteractor.getFilterParameters()
        _filtersState.value = getFiltersState()
    }

    fun hideKeyboard() {
        viewModelScope.launch {
            _hideKeyboardState.emit(Unit)
        }
    }

    fun clearLocation() {
        currentFilters = currentFilters.copy(area = null)
        saveAndRenderState()
    }

    fun clearIndustry() {
        currentFilters = currentFilters.copy(industry = null)
        saveAndRenderState()
    }

    fun toggleDoNotShowWithoutSalary(isChecked: Boolean) {
        currentFilters = currentFilters.copy(onlyWithSalary = isChecked)
        saveAndRenderState()
    }

    fun resetFilters() {
        currentFilters = currentFilters.copy(
            area = null,
            industry = null,
            salary = null,
            onlyWithSalary = false
        )
        saveAndRenderState()
    }

    fun saveSalary(salaryText: String) {
        currentFilters = currentFilters.copy(
            salary = if (salaryText.isEmpty()) null else salaryText.toInt()
        )
        saveAndRenderState()
    }
    // endregion

    // region Приватные методы
    private fun getFiltersState(): FiltersState {
        return FiltersState(
            areFiltersSet = filtersInteractor.isFilterEnabled(),
            location = currentFilters.area?.name.orEmpty(),
            industry = currentFilters.industry?.name.orEmpty(),
            salary = if (currentFilters.salary == null) "" else currentFilters.salary.toString(),
            doNotShowWithoutSalary = currentFilters.onlyWithSalary
        )
    }

    private fun saveAndRenderState() {
        filtersInteractor.saveFilterParameters(currentFilters)
        _filtersState.value = getFiltersState()
    }
    // endregion

}
