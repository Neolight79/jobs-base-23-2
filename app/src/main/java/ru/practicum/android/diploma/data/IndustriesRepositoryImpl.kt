package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.FilterIndustryResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_NOT_FOUND_404
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_OK_200
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_SERVICE_UNAVAILABLE_503
import ru.practicum.android.diploma.domain.api.IndustriesRepository
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.SearchResultStatus
import ru.practicum.android.diploma.util.mappers.FilterIndustryMapper

class IndustriesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: FilterIndustryMapper
) : IndustriesRepository {

    override suspend fun getIndustries(): Pair<List<FilterIndustry>?, SearchResultStatus> {
        val response = networkClient.getIndustries(null)

        return when {
            response.resultCode == HTTP_OK_200 && response is FilterIndustryResponse -> {
                val industries = response.results.map { mapper.map(it) }
                Pair(industries, SearchResultStatus.Success)
            }
            response.resultCode == HTTP_SERVICE_UNAVAILABLE_503 -> {
                Pair(null, SearchResultStatus.NoConnection)
            }
            response.resultCode == HTTP_NOT_FOUND_404 -> {
                Pair(null, SearchResultStatus.NotFound)
            }
            else -> {
                Pair(null, SearchResultStatus.ServerError)
            }
        }
    }
}
