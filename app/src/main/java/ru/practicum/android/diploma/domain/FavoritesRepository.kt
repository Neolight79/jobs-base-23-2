package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.VacancyFavoriteEntity
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavoritesRepository {
    suspend fun addVacancyToFavorites(vacancy: Vacancy)
    suspend fun deleteVacancyFromFavorites(vacancy: Vacancy)
    fun getFavoriteVacancies(): Flow<List<Vacancy>>
    suspend fun isFavorite(vacancyId: String): Boolean
    suspend fun getFavoriteById(vacancyId: String): Vacancy
}
