package ru.practicum.android.diploma.domain.models

data class Area (
    val id: Int,
    val name: String?,
    val parentId: Integer?,
    val areas: List<Area>?
)
