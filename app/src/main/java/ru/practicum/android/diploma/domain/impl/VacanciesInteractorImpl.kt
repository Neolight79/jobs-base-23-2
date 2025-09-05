package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.HTTP_OK_200
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
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
    ): List<Vacancy> {
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

            if (response.resultCode == HTTP_OK_200 && response is VacanciesResponse) {
                totalPages = response.pages
                currentPage = response.page
                return response.items.map { vacancyMapper.map(it) }
            }

            return emptyList()
        } finally {
            isLoading = false
        }
    }

    fun resetPagination() {
        currentPage = 1
        totalPages = 1
    }
}

