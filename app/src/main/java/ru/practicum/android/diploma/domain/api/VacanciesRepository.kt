package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.data.dto.Response

interface VacanciesRepository {
    suspend fun getVacancies(
        area: Int? = null,
        industry: Int? = null,
        text: String? = null,
        salary: Int? = null,
        page: Int = 1,
        onlyWithSalary: Boolean? = null
    ): Response

    suspend fun getVacancyById(id: String): Response
}
