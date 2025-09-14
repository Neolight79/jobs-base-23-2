package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.domain.models.CountryAndRegion
import ru.practicum.android.diploma.domain.models.CountryAndRegionDto

class CountryAndRegionMapper {

    fun map(area: CountryAndRegion): CountryAndRegionDto {
        return CountryAndRegionDto(
            countryId = area.countryId,
            countryName = area.countryName,
            regionId = area.regionId,
            regionName = area.regionName
        )
    }

    fun map(area: CountryAndRegionDto): CountryAndRegion {
        return CountryAndRegion(
            countryId = area.countryId,
            countryName = area.countryName,
            regionId = area.regionId,
            regionName = area.regionName
        )
    }

}
