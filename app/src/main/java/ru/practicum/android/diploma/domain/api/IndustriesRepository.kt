package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.SearchResultStatus

interface IndustriesRepository {

    suspend fun getIndustries(): Pair<List<FilterIndustry>?, SearchResultStatus>
}
