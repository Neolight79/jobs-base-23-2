package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.domain.models.Industry

class IndustryMapper {

    fun map(industry: Industry): IndustryDto {
        return IndustryDto(
            id = industry.id,
            name = industry.name
        )
    }

    fun map(industryDto: IndustryDto): Industry {
        return Industry(
            id = industryDto.id,
            name = industryDto.name
        )
    }

}
