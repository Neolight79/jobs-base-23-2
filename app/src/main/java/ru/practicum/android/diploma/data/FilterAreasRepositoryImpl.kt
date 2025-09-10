package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.FilterAreaResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_OK_200
import ru.practicum.android.diploma.domain.api.FilterAreasRepository
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.util.mappers.FilterAreaMapper

class FilterAreasRepositoryImpl(
    private val networkClient: NetworkClient,
    private val filterAreaMapper: FilterAreaMapper
) : FilterAreasRepository {

    override suspend fun getFilterAreas(): List<FilterArea>? {
        val response = networkClient.getAreas(null)
        if (response.resultCode != HTTP_OK_200) return null
        return (response as FilterAreaResponse).results.map {
            filterAreaMapper.map(it)
        }
    }

}
