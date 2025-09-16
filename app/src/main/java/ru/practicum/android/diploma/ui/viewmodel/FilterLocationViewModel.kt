package ru.practicum.android.diploma.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.domain.models.CountryAndRegion
import ru.practicum.android.diploma.domain.models.FilterLocationState
import ru.practicum.android.diploma.domain.models.Location

class FilterLocationViewModel(
    private val filtersInteractor: FilterParametersInteractor
) : ViewModel() {

    // Переменная текущих настроек из хранилища
    private var currentFilters = filtersInteractor.getFilterParameters()

    // StateFlow для состояния экрана настройки фильтра места работы
    private val _filterLocationState = MutableStateFlow<FilterLocationState>(getFilterLocationState())
    val filterLocationState: StateFlow<FilterLocationState> = _filterLocationState.asStateFlow()

    fun clearCountry() {
        currentFilters = currentFilters.copy(
            area = null
        )
        _filterLocationState.value = getFilterLocationState()
    }

    fun clearRegion() {
        currentFilters = currentFilters.copy(
            area = CountryAndRegion(
                countryId = currentFilters.area?.countryId,
                countryName = currentFilters.area?.countryName,
                regionId = null,
                regionName = null
            )
        )
        _filterLocationState.value = getFilterLocationState()
    }

    fun updateCountry(country: Location) {
        currentFilters = currentFilters.copy(
            area = CountryAndRegion(
                countryId = country.id,
                countryName = country.name,
                regionId = null,
                regionName = null
            )
        )
        _filterLocationState.value = getFilterLocationState()
    }

    fun updateRegion(region: CountryAndRegion) {
        currentFilters = currentFilters.copy(
            area = region
        )
        _filterLocationState.value = getFilterLocationState()
    }

    fun saveState() {
        filtersInteractor.saveFilterParameters(currentFilters)
    }
    // endregion

    // region Приватные методы
    private fun getFilterLocationState(): FilterLocationState {
        return if (currentFilters.area == null) {
            FilterLocationState(
                isDataSelected = false,
                countryId = null,
                countryName = "",
                region = ""
            )
        } else {
            FilterLocationState(
                isDataSelected = currentFilters.area?.regionId != null,
                countryId = currentFilters.area?.countryId,
                countryName = currentFilters.area?.countryName.orEmpty(),
                region = currentFilters.area?.regionName.orEmpty()
            )
        }
    }
    // endregion

}
