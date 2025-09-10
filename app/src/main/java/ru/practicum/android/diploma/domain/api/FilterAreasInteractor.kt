package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterArea

interface FilterAreasInteractor {

    fun getFilterAreasCountries(): List<FilterArea>

    fun getFilterAreasFiltered(query: String?): List<FilterArea>
}
