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
import ru.practicum.android.diploma.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.IndustriesState
import ru.practicum.android.diploma.domain.models.SearchResultStatus

class FilterIndustryViewModel(
    private val industriesInteractor: IndustriesInteractor,
    private val filtersInteractor: FilterParametersInteractor
) : ViewModel() {

    // Переменная текущих настроек из хранилища
    private var selectedIndustry = filtersInteractor.getFilterParameters().industry

    // Текущее значение строки поиска
    private var latestSearchText = ""

    // StateFlow для состояния экрана выбора отрасли
    private val _industryState = MutableStateFlow<IndustriesState>(IndustriesState.Loading)
    val industryState: StateFlow<IndustriesState> = _industryState.asStateFlow()

    // StateFlow для строки поиска
    private val _searchTextState = MutableStateFlow(latestSearchText)
    val searchTextState: StateFlow<String> = _searchTextState.asStateFlow()

    // StateFlow для сокрытия клавиатуры
    private val _hideKeyboardState = MutableSharedFlow<Unit>(replay = 0)
    val hideKeyboardState = _hideKeyboardState.asSharedFlow()

    fun clearSearch() {
        latestSearchText = ""
        _searchTextState.value = ""
        search()
    }

    fun hideKeyboard() {
        viewModelScope.launch {
            _hideKeyboardState.emit(Unit)
        }
    }

    fun search() {
        _industryState.value = IndustriesState.Loading
        viewModelScope.launch {
            val response = industriesInteractor.getIndustries(
                query = if (latestSearchText.isEmpty()) null else latestSearchText
            )
            processResult(response.first, response.second)
        }
    }

    fun onSearchTextChange(newText: String) {
        _searchTextState.value = newText
        latestSearchText = newText
        search()
    }

    fun updateSelected(selectedItem: FilterIndustry) {
        selectedIndustry = selectedItem
    }

    fun saveState() {
        val currentFilters = filtersInteractor.getFilterParameters().copy(
            industry = selectedIndustry,
        )
        filtersInteractor.saveFilterParameters(currentFilters)
    }

    private fun processResult(foundIndustries: List<FilterIndustry>?, searchStatus: SearchResultStatus) {
        when (searchStatus) {
            SearchResultStatus.Success -> {
                if (foundIndustries == null) {
                    _industryState.value = IndustriesState.EmptyResult
                } else {
                    _industryState.value = IndustriesState.Industries(
                        selectedItem = selectedIndustry,
                        industries = foundIndustries
                    )
                }
            }
            else -> {
                _industryState.value = IndustriesState.ServerError
            }
        }
    }

}
