package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse

interface VacanciesRepository {
    suspend fun getVacancies(
        area: Int? = null,
        industry: Int? = null,
        text: String? = null,
        salary: Int? = null,
        page: Int = 1,
        onlyWithSalary: Boolean? = null
    ): VacanciesResponse

    suspend fun getVacancyById(id: String): VacancyDetailResponse
}
