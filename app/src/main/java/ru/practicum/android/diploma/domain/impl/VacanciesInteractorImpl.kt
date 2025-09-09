package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_BAD_REQUEST_400
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_NOT_FOUND_404
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_OK_200
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_SERVICE_UNAVAILABLE_503
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.SearchResultStatus
import ru.practicum.android.diploma.domain.models.VacanciesPage
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.mappers.VacancyMapper

class VacanciesInteractorImpl(
    private val networkClient: NetworkClient,
    private val vacancyMapper: VacancyMapper
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
            val requestOptions = mutableMapOf<String, String>()
            area?.let { requestOptions["area"] = it }
            industry?.let { requestOptions["industry"] = it }
            text?.let { requestOptions["text"] = it }
            salary?.let { requestOptions["salary"] = it.toString() }
            requestOptions["only_with_salary"] = onlyWithSalary.toString()
            requestOptions["page"] = page.toString()

            val request = Request(options = requestOptions)
            val response = networkClient.getVacancies(request)

            return if (response.resultCode == HTTP_OK_200 && response is VacanciesResponse) {
                totalPages = response.pages
                currentPage = response.page
                val currentPage = if (response.items.isEmpty()) {
                    null
                } else {
                    VacanciesPage(
                        totalVacancies = response.found,
                        pageNumber = page,
                        vacancies = response.items.map { vacancyMapper.map(it) }
                    )
                }
                val currentStatus = SearchResultStatus.Success
                Pair(currentPage, currentStatus)
            } else if (response.resultCode == HTTP_SERVICE_UNAVAILABLE_503) {
                Pair(null, SearchResultStatus.NoConnection)
            } else {
                Pair(null, SearchResultStatus.ServerError)
            }
        } finally {
            isLoading = false
        }
    }

    override suspend fun getVacancyById(id: String): Pair<Vacancy?, SearchResultStatus> {
        val request = Request(options = mapOf("id" to id))
        val response = networkClient.getVacancyById(request)

        return if (response.resultCode == HTTP_OK_200 && response is VacancyDetailResponse) {
            val vacancyDto = vacancyMapper.detailResponseToDto(response)
            val vacancy = vacancyMapper.map(vacancyDto)
            Pair(vacancy, SearchResultStatus.Success)
        } else if (response.resultCode == HTTP_SERVICE_UNAVAILABLE_503) {
            Pair(null, SearchResultStatus.NoConnection)
        } else if (response.resultCode == HTTP_BAD_REQUEST_400 || response.resultCode == HTTP_NOT_FOUND_404) {
            Pair(null, SearchResultStatus.NotFound)
        } else {
            Pair(null, SearchResultStatus.ServerError)
        }
    }

}
