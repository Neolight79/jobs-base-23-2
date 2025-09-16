package ru.practicum.android.diploma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterAreasInteractor
import ru.practicum.android.diploma.domain.models.CountryAndRegion
import ru.practicum.android.diploma.domain.models.RegionsState
import ru.practicum.android.diploma.domain.models.SearchResultStatus

class FilterLocationRegionViewModel(
    private val countryID: Int,
    private val filterAreasInteractor: FilterAreasInteractor
) : ViewModel() {

    // Текущее значение строки поиска
    private var latestSearchText = ""

    // StateFlow для состояния экрана поиска вакансий
    private val _regionsState = MutableStateFlow<RegionsState>(RegionsState.Loading)
    val regionsState: StateFlow<RegionsState> = _regionsState.asStateFlow()

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
        _regionsState.value = RegionsState.Loading
        viewModelScope.launch {
            val response = filterAreasInteractor.getFilterAreasFiltered(
                parentId = if (countryID == 0) null else countryID,
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

    private fun processResult(foundRegions: List<CountryAndRegion>?, searchStatus: SearchResultStatus) {
        when (searchStatus) {
            SearchResultStatus.Success -> {
                if (foundRegions == null) {
                    _regionsState.value = RegionsState.EmptyResult
                } else {
                    _regionsState.value = RegionsState.Regions(
                        regions = foundRegions
                    )
                }
            }
            else -> {
                _regionsState.value = RegionsState.ServerError
            }
        }
    }

}
