package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.FilterParametersDto
import ru.practicum.android.diploma.domain.models.FilterParameters

class FilterParametersMapper(
    private val areaMapper: AreaMapper,
    private val industryMapper: IndustryMapper
) {

    fun map(filterParameters: FilterParameters): FilterParametersDto {
        return FilterParametersDto(
            area = filterParameters.area?.let { area -> areaMapper.map(area) },
            industry = filterParameters.industry?.let { industry -> industryMapper.map(industry) },
            salary = filterParameters.salary,
            onlyWithSalary = filterParameters.onlyWithSalary,
        )
    }

    fun map(filterParametersDto: FilterParametersDto): FilterParameters {
        return FilterParameters(
            area = filterParametersDto.area?.let { area -> areaMapper.map(area) },
            industry = filterParametersDto.industry?.let { industry -> industryMapper.map(industry) },
            salary = filterParametersDto.salary,
            onlyWithSalary = filterParametersDto.onlyWithSalary,
        )
    }

}
