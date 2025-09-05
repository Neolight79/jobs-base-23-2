package ru.practicum.android.diploma.domain.models

data class Address(
    val id: String,
    val city: String?,
    val street: String?,
    val building: String?,
    val fullAddress: String?,
)
