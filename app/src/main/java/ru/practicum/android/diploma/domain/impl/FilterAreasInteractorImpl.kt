package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FilterAreasInteractor
import ru.practicum.android.diploma.domain.api.FilterAreasRepository
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.SearchResultStatus

class FilterAreasInteractorImpl(
    private val filterAreasRepository: FilterAreasRepository
) : FilterAreasInteractor{

    override suspend fun getFilterAreasCountries(): Pair<List<FilterArea>?, SearchResultStatus> {
        val filterAreas = filterAreasRepository.getFilterAreas()
        return if (filterAreas == null) Pair(null, SearchResultStatus.ServerError)
        else Pair(filterAreas.filter { it.parentId == null }, SearchResultStatus.Success)
    }

    override suspend fun getFilterAreasFiltered(query: String?): Pair<List<FilterArea>?, SearchResultStatus> {
        val filterAreas = filterAreasRepository.getFilterAreas()
        return if (filterAreas == null) Pair(null, SearchResultStatus.ServerError)
        else Pair(
            filterAreas.filter { it.parentId != null && it.name == query },
            SearchResultStatus.Success
        )
    }
}
