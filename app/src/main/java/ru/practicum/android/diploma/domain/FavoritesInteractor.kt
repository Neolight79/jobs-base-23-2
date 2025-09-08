package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavoritesInteractor {
    suspend fun addVacancyToFavorites(vacancy: Vacancy)
    suspend fun deleteVacancyFromFavorites(vacancy: Vacancy)
    fun getFavoriteVacancyById(vacancyId: String): Flow<Vacancy?>
    fun getFavoriteVacancies(): Flow<List<Vacancy>>
}
