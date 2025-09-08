package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.SearchResultStatus
import ru.practicum.android.diploma.domain.models.VacanciesPage
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacanciesInteractor {
    suspend fun searchVacancies(
        area: String? = null,
        industry: String? = null,
        text: String? = null,
        salary: Int? = null,
        onlyWithSalary: Boolean = false,
        page: Int
    ): Pair<VacanciesPage?, SearchResultStatus>

    suspend fun getVacancyById(id: String): Pair<Vacancy?, SearchResultStatus>
    suspend fun isFavorite(vacancyId: String): Boolean
    suspend fun addToFavorites(vacancy: Vacancy)
    suspend fun removeFromFavorites(vacancyId: String)
}
