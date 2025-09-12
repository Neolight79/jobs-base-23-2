package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.domain.api.IndustriesRepository
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.SearchResultStatus

class IndustriesInteractorImpl(
    private val repository: IndustriesRepository
) : IndustriesInteractor {

    override suspend fun getIndustries(query: String?): Pair<List<FilterIndustry>?, SearchResultStatus> {
        val (industries, status) = repository.getIndustries()

        val filtered = if (!industries.isNullOrEmpty() && !query.isNullOrBlank()) {
            industries.filter { it.name?.contains(query, ignoreCase = true) == true }
        } else {
            industries
        }

        return Pair(filtered, status)
    }
}
