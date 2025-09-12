package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.SearchResultStatus

interface IndustriesInteractor {

    suspend fun getIndustries(query: String?): Pair<List<FilterIndustry>?, SearchResultStatus>
}
