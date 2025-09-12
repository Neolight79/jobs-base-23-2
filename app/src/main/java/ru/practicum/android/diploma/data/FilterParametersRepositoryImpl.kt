package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.FilterAreaDto
import ru.practicum.android.diploma.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.FilterParametersDto
import ru.practicum.android.diploma.domain.api.FilterParametersRepository
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.util.mappers.FilterParametersMapper

class FilterParametersRepositoryImpl(
    private val sharedStorage: SharedStorage,
    private val filterParametersMapper: FilterParametersMapper
) : FilterParametersRepository {

    override fun saveFilterParameters(filterParameters: FilterParameters) {
        val dto = filterParametersMapper.map(filterParameters)
        val idOnlyDto = dto.copy(
            area = dto.area?.let { FilterAreaDto(id = it.id, name = null, parentId = null, areas = null) },
            industry = dto.industry?.let { FilterIndustryDto(id = it.id, name = null) }
        )
        sharedStorage.putData(idOnlyDto)
    }

    override fun getFilterParameters(): FilterParameters {
        val filterParametersDto = sharedStorage.getData(
            FilterParametersDto(null, null, null, false)
        ) as FilterParametersDto
        return filterParametersMapper.map(filterParametersDto)
    }

}
