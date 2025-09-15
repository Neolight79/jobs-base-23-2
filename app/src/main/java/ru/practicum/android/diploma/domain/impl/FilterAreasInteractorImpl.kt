package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FilterAreasInteractor
import ru.practicum.android.diploma.domain.api.FilterAreasRepository
import ru.practicum.android.diploma.domain.models.CountryAndRegion
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
    ): Pair<List<CountryAndRegion>?, SearchResultStatus> {
        if (filterAreas.isEmpty()) filterAreas = filterAreasRepository.getFilterAreas().orEmpty()
        if (filterAreas.isEmpty()) return Pair(null, SearchResultStatus.ServerError)
        val areas = mutableListOf<FilterArea>()
        filterAreas.forEach { parent -> parent.areas?.let { areas += it } }
        val locations = when {
            parentId != null && !query.isNullOrEmpty() ->
                mapToLocations(
                    areas.filter { it.parentId == parentId &&
                        it.name?.contains(query, ignoreCase = true) == true }
                )
            parentId != null && query.isNullOrEmpty() ->
                mapToLocations(
                    areas.filter { it.parentId == parentId }
                )
            parentId == null && !query.isNullOrEmpty() ->
                mapToLocations(
                    areas.filter { it.name?.contains(query, ignoreCase = true) == true }
                )
            else -> mapToLocations(areas)
        }
        return Pair(locations, SearchResultStatus.Success)
    }

    private fun mapToLocations(areas: List<FilterArea>): List<CountryAndRegion> {
        return areas.map { region ->
            CountryAndRegion(
                countryId = filterAreas.first { it.id == region.parentId }.id,
                countryName = filterAreas.first { it.id == region.parentId }.name,
                regionId = region.id,
                regionName = region.name.orEmpty()
            )
        }
    }

}
