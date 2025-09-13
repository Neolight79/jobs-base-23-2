package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.FilterParametersDto
import ru.practicum.android.diploma.domain.api.FilterParametersRepository
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.util.mappers.FilterParametersMapper

class FilterParametersRepositoryImpl(
    private val sharedStorage: SharedStorage,
    private val filterParametersMapper: FilterParametersMapper
) : FilterParametersRepository {

    override fun saveFilterParameters(filterParameters: FilterParameters) {
        val dto = filterParametersMapper.toStorageDto(filterParameters)
        sharedStorage.putData(dto)
    }

    override fun getFilterParameters(): FilterParameters {
        val dto = sharedStorage.getData(FilterParametersDto(null, null, null, false))
        return filterParametersMapper.map(dto)
    }
}
