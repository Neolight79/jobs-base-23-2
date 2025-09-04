package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterParameters

interface FilterParametersRepository {

    fun saveFilterParameters(filterParameters: FilterParameters)

    fun getFilterParameters(): FilterParameters

}
