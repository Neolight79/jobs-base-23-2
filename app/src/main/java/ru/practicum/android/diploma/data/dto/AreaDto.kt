package ru.practicum.android.diploma.data.dto

data class AreaDto(
    val id: Int,
    val name: String?,
    val parentId: Integer?,
    val areas: List<AreaDto>?
)
