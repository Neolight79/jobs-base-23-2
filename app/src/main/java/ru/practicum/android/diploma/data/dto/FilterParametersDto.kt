package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.domain.models.CountryAndRegionDto

data class FilterParametersDto(
    val area: CountryAndRegionDto?,
    val industry: FilterIndustryDto?,
    val salary: Int?,
    val onlyWithSalary: Boolean = false,
)
