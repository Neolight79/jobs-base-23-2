package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_NOT_FOUND_404
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_OK_200
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_SERVICE_UNAVAILABLE_503
import ru.practicum.android.diploma.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.SearchResultStatus
import ru.practicum.android.diploma.domain.models.VacanciesPage
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.mappers.VacancyMapper

class VacanciesInteractorImpl(
    private val repository: VacanciesRepository,
    private val vacancyMapper: VacancyMapper,
    private val filterInteractor: FilterParametersInteractor
) : VacanciesInteractor {

    private var currentPage = 1
    private var totalPages = 1
    private var isLoading = false

    override suspend fun searchVacancies(
        area: String?,
        industry: String?,
        text: String?,
        salary: Int?,
        onlyWithSalary: Boolean,
        page: Int
    ): Pair<VacanciesPage?, SearchResultStatus> {
        isLoading = true
        try {
            val saved = filterInteractor.getFilterParameters()
            val areaId = if (saved.area != null) {
                saved.area.regionId ?: saved.area.countryId
            } else {
                null
            }

            val area: Int? = if (area != null) area.toIntOrNull() else areaId
            val industry: Int? =
                (industry ?: saved.industry?.id?.toString())?.toIntOrNull()
            val salary: Int? = salary ?: saved.salary
            val onlyWithSalary: Boolean = onlyWithSalary || saved.onlyWithSalary

            val response = repository.getVacancies(
                area,
                industry,
                text,
                salary,
                page,
                onlyWithSalary
            )
            return when {
                response.resultCode == HTTP_OK_200 && response is VacanciesResponse -> {
                    totalPages = response.pages
                    currentPage = response.page

                    val vacanciesPage = if (response.items.isEmpty()) {
                        null
                    } else {
                        VacanciesPage(
                            totalVacancies = response.found,
                            pageNumber = page,
                            vacancies = response.items.map { vacancyMapper.map(it) }
                        )
                    }

                    vacanciesPage to SearchResultStatus.Success
                }

                response.resultCode == HTTP_SERVICE_UNAVAILABLE_503 -> null to SearchResultStatus.NoConnection
                response.resultCode == HTTP_NOT_FOUND_404 -> null to SearchResultStatus.NotFound
                else -> null to SearchResultStatus.ServerError
            }
        } finally {
            isLoading = false
        }
    }

    override suspend fun getVacancyById(id: String): Pair<Vacancy?, SearchResultStatus> {
        val response = repository.getVacancyById(id)

        return when (response.resultCode) {
            HTTP_OK_200 -> {
                val detail = response as? VacancyDetailResponse
                    ?: return null to SearchResultStatus.ServerError
                val vacancy = vacancyMapper.detailResponseToDto(detail)
                    .let { vacancyMapper.map(it) }
                vacancy to SearchResultStatus.Success
            }

            HTTP_SERVICE_UNAVAILABLE_503 -> null to SearchResultStatus.NoConnection
            HTTP_NOT_FOUND_404 -> null to SearchResultStatus.NotFound
            else -> null to SearchResultStatus.ServerError
        }
    }
}
