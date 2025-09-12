package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_NOT_FOUND_404
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_OK_200
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_SERVICE_UNAVAILABLE_503
import ru.practicum.android.diploma.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.SearchResultStatus
import ru.practicum.android.diploma.domain.models.VacanciesPage
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.mappers.VacancyMapper

class VacanciesInteractorImpl(
    private val networkClient: NetworkClient,
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
            val requestOptions = mutableMapOf<String, String>()
            area?.let { requestOptions[PARAM_AREA] = it }
            industry?.let { requestOptions[PARAM_INDUSTRY] = it }
            text?.let { requestOptions[PARAM_TEXT] = it }
            salary?.let { requestOptions[PARAM_SALARY] = it.toString() }
            requestOptions[PARAM_ONLY_WITH_SALARY] = onlyWithSalary.toString()
            requestOptions[PARAM_PAGE] = page.toString()

            val saved = filterInteractor.getFilterParameters()
            if (!requestOptions.containsKey(PARAM_AREA)) {
                saved.area?.id?.let { requestOptions[PARAM_AREA] = it.toString() }
            }
            if (!requestOptions.containsKey(PARAM_INDUSTRY)) {
                saved.industry?.id?.let { requestOptions[PARAM_INDUSTRY] = it.toString() }
            }
            if (!requestOptions.containsKey(PARAM_SALARY)) {
                saved.salary?.let { requestOptions[PARAM_SALARY] = it.toString() }
            }
            requestOptions[PARAM_ONLY_WITH_SALARY] = (onlyWithSalary || saved.onlyWithSalary).toString()

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
        } else if (response.resultCode == HTTP_NOT_FOUND_404) {
            Pair(null, SearchResultStatus.NotFound)
        } else {
            Pair(null, SearchResultStatus.ServerError)
        }
    }

    companion object {
        private const val PARAM_AREA = "area"
        private const val PARAM_INDUSTRY = "industry"
        private const val PARAM_TEXT = "text"
        private const val PARAM_SALARY = "salary"
        private const val PARAM_ONLY_WITH_SALARY = "only_with_salary"
        private const val PARAM_PAGE = "page"
    }
}
