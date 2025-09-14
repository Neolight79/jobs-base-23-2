package ru.practicum.android.diploma.domain.models

data class FilterParameters(
    val area: CountryAndRegion?,
    val industry: FilterIndustry?,
    val salary: Int?,
    val onlyWithSalary: Boolean = false,
)
