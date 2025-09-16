package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterArea

interface FilterAreasRepository {

    suspend fun getFilterAreas(): List<FilterArea>?

}
