package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.domain.api.FilterParametersRepository
import ru.practicum.android.diploma.domain.models.FilterParameters

class FilterParametersInteractorImpl(
    private val filterParametersRepository: FilterParametersRepository
) : FilterParametersInteractor {

    override fun saveFilterParameters(filterParameters: FilterParameters) {
        filterParametersRepository.saveFilterParameters(filterParameters)
    }

    override fun getFilterParameters(): FilterParameters {
        return filterParametersRepository.getFilterParameters()
    }

    override fun isFilterEnabled(): Boolean {
        val filterParameters = getFilterParameters()
        return filterParameters.area != null
            || filterParameters.industry != null
            || filterParameters.salary != null
            || filterParameters.onlyWithSalary
    }
}
