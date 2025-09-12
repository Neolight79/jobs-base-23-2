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
        return when {
            (filterAreas == null) -> Pair(null, SearchResultStatus.ServerError)
            else -> Pair(
                filterAreas.filter { it.parentId == null }
                .map { (id, name, parentId, areas) -> FilterArea(id, name, parentId, listOf()) },
                SearchResultStatus.Success
            )
        }
    }

    override suspend fun getFilterAreasFiltered(parentId: Int?, query: String?): Pair<List<FilterArea>?, SearchResultStatus> {
        val filterAreas = filterAreasRepository.getFilterAreas()
        if (filterAreas == null) return Pair(null, SearchResultStatus.ServerError)
        val areas = mutableListOf<FilterArea>()
        filterAreas.forEach { parent -> parent.areas?.let { areas += it } }
        return when {
            parentId != null && !query.isNullOrEmpty() -> Pair(
                areas.filter { it.parentId == parentId &&
                    it.name?.contains(query, ignoreCase = true) == true },
                SearchResultStatus.Success
            )
            parentId != null && query.isNullOrEmpty() -> Pair(
                areas.filter { it.parentId == parentId },
                SearchResultStatus.Success
            )
            parentId == null && !query.isNullOrEmpty() -> Pair(
                areas.filter { it.name?.contains(query, ignoreCase = true) == true },
                SearchResultStatus.Success
            )
            else -> Pair(areas, SearchResultStatus.Success)
        }
    }
}
