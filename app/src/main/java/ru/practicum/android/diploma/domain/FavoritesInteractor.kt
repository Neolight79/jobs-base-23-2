package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavoritesInteractor {
    suspend fun addVacancyToFavorites(vacancy: Vacancy)
    suspend fun deleteVacancyFromFavorites(vacancy: Vacancy)
    fun getFavoriteVacancies(): Flow<List<Vacancy>>
    suspend fun checkIsFavorite(vacancyId: String): Boolean
    suspend fun getFavoriteById(vacancyId: String): Vacancy
}
