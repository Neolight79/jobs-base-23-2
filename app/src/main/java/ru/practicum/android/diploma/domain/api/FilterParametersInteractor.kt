package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterParameters

interface FilterParametersInteractor {

    fun saveFilterParameters(filterParameters: FilterParameters)

    fun getFilterParameters(): FilterParameters

    fun isFilterEnabled(): Boolean

}
