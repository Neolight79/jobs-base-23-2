package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.VacanciesRepository

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient
) : VacanciesRepository {

    override suspend fun getVacancies(
        area: Int?,
        industry: Int?,
        text: String?,
        salary: Int?,
        page: Int,
        onlyWithSalary: Boolean?
    ): Response {
        val options = mutableMapOf<String, String>().apply {
            area?.let { put("area", it.toString()) }
            industry?.let { put("industry", it.toString()) }
            text?.let { put("text", it) }
            salary?.let { put("salary", it.toString()) }
            put("page", page.toString())
            onlyWithSalary?.let { put("only_with_salary", it.toString()) }
        }

        return networkClient.getVacancies(Request(options))
    }

    override suspend fun getVacancyById(id: String): Response {
        return networkClient.getVacancyById(Request(mapOf("id" to id)))
    }
}
