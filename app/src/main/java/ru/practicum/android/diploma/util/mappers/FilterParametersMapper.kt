package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.FilterParametersDto
import ru.practicum.android.diploma.domain.models.FilterParameters

class FilterParametersMapper(
    private val filterAreaMapper: FilterAreaMapper,
    private val filterIndustryMapper: FilterIndustryMapper
) {

    fun map(filterParameters: FilterParameters): FilterParametersDto {
        return FilterParametersDto(
            area = filterParameters.area?.let { area -> filterAreaMapper.map(area) },
            industry = filterParameters.industry?.let { industry -> filterIndustryMapper.map(industry) },
            salary = filterParameters.salary,
            onlyWithSalary = filterParameters.onlyWithSalary,
        )
    }

    fun map(filterParametersDto: FilterParametersDto): FilterParameters {
        return FilterParameters(
            area = filterParametersDto.area?.let { area -> filterAreaMapper.map(area) },
            industry = filterParametersDto.industry?.let { industry -> filterIndustryMapper.map(industry) },
            salary = filterParametersDto.salary,
            onlyWithSalary = filterParametersDto.onlyWithSalary,
        )
    }

}
