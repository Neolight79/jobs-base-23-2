package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Location
import ru.practicum.android.diploma.domain.models.SearchResultStatus

interface FilterAreasInteractor {

    suspend fun getFilterAreasCountries(): Pair<List<Location>?, SearchResultStatus>

    suspend fun getFilterAreasFiltered(
        parentId: Int? = null,
        query: String? = null
    ): Pair<List<Location>?, SearchResultStatus>

}
