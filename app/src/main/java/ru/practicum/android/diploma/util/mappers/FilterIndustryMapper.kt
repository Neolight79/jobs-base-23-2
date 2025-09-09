package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.domain.models.FilterIndustry

class FilterIndustryMapper {

    fun map(filterIndustry: FilterIndustry): FilterIndustryDto {
        return FilterIndustryDto(
            id = filterIndustry.id,
            name = filterIndustry.name
        )
    }

    fun map(filterIndustryDto: FilterIndustryDto): FilterIndustry {
        return FilterIndustry(
            id = filterIndustryDto.id,
            name = filterIndustryDto.name
        )
    }

}
