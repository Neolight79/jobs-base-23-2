package ru.practicum.android.diploma.data.dto

data class FilterParametersDto(
    val area: AreaDto?,
    val industry: IndustryDto?,
    val salary: Int?,
    val onlyWithSalary: Boolean = false,
)

