package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.SearchResultStatus

interface FilterAreasInteractor {

    suspend fun getFilterAreasCountries(): Pair<List<FilterArea>?, SearchResultStatus>

    suspend fun getFilterAreasFiltered(
        parentId: Int? = null,
        query: String? = null
    ): Pair<List<FilterArea>?, SearchResultStatus>

}
