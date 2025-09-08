package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.VacancyFavoriteEntity
import ru.practicum.android.diploma.domain.FavoritesInteractor
import ru.practicum.android.diploma.domain.FavoritesRepository
import ru.practicum.android.diploma.domain.models.Vacancy

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {

    override suspend fun addVacancyToFavorites(vacancy: Vacancy) {
        favoritesRepository.addVacancyToFavorites(vacancy)
    }

    override suspend fun deleteVacancyFromFavorites(vacancy: Vacancy) {
        favoritesRepository.deleteVacancyFromFavorites(vacancy)
    }

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return favoritesRepository.getFavoriteVacancies()
    }

    override suspend fun checkIsFavorite(vacancyId: String): Boolean {
        return favoritesRepository.isFavorite(vacancyId)
    }

    override suspend fun getFavoriteById(vacancyId: String): Vacancy {
        return favoritesRepository.getFavoriteById(vacancyId)
    }
}
