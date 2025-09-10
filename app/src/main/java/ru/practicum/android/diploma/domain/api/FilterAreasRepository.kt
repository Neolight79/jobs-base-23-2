package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterArea

interface FilterAreasRepository {

    fun getFilterAreas(): List<FilterArea>

}
