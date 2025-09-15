package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FilterAreasInteractor
import ru.practicum.android.diploma.domain.api.FilterAreasRepository
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.Location
import ru.practicum.android.diploma.domain.models.SearchResultStatus

class FilterAreasInteractorImpl(
    private val filterAreasRepository: FilterAreasRepository
) : FilterAreasInteractor {

    private var filterAreas: List<FilterArea> = emptyList()

    override suspend fun getFilterAreasCountries(): Pair<List<Location>?, SearchResultStatus> {
        if (filterAreas.isEmpty()) filterAreas = filterAreasRepository.getFilterAreas().orEmpty()
        return when {
            filterAreas.isEmpty() -> Pair(null, SearchResultStatus.ServerError)
            else -> Pair(
                filterAreas.filter { it.parentId == null }
                    .map { (id, name) -> Location(id, name.orEmpty()) },
                SearchResultStatus.Success
            )
        }
    }

    override suspend fun getFilterAreasFiltered(
        parentId: Int?,
        query: String?
    ): Pair<List<Location>?, SearchResultStatus> {
        if (filterAreas.isEmpty()) {
            filterAreas = filterAreasRepository.getFilterAreas().orEmpty()
        }
        if (filterAreas.isEmpty()) {
            return null to SearchResultStatus.ServerError
        }

        val areas = filterAreas.flatMap { it.areas.orEmpty() }

        val filtered = areas
            .asSequence()
            .filter { parentId == null || it.parentId == parentId }
            .filter { query.isNullOrBlank() || it.name?.contains(query, ignoreCase = true) == true }
            .toList()

        return mapToLocations(filtered) to SearchResultStatus.Success
    }

    private fun mapToLocations(areas: List<FilterArea>): List<Location> {
        return areas.map {
            Location(
                id = it.id,
                name = it.name.orEmpty()
            )
        }
    }

}
