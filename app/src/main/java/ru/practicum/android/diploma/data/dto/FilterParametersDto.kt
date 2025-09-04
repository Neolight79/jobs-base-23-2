package ru.practicum.android.diploma.data.dto

data class FilterParametersDto(
    val area: FilterAreaDto?,
    val industry: FilterIndustryDto?,
    val salary: Int?,
    val onlyWithSalary: Boolean = false,
)

