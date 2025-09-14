package ru.practicum.android.diploma.domain.models

data class FilterLocationState(
    val isDataSelected: Boolean,
    val countryId: Int?,
    val countryName: String,
    val region: String
)
