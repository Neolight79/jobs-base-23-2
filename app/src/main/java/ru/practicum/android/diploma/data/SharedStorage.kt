package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.FilterParametersDto

interface SharedStorage {
    fun putData(data: FilterParametersDto)
    fun getData(defaultData: FilterParametersDto): FilterParametersDto
}
