package ru.practicum.android.diploma.domain.models

data class Salary(
    val id: String,
    val currency: String,
    val from: Int?,
    val to: Int?
)
