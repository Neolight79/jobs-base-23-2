package ru.practicum.android.diploma.domain.models

data class FiltersState(
    val areFiltersSet: Boolean,
    val location: String,
    val industry: String,
    val salary: String,
    val doNotShowWithoutSalary: Boolean
)
