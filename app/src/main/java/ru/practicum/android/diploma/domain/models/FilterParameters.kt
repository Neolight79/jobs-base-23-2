package ru.practicum.android.diploma.domain.models

data class FilterParameters(
    val area: Area?,
    val industry: Industry?,
    val salary: Int?,
    val onlyWithSalary: Boolean = false,
)
