package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
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

    override fun getFavoriteVacancyById(vacancyId: String): Flow<Vacancy?> {
        return favoritesRepository.getFavoriteVacancyById(vacancyId)
    }

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return favoritesRepository.getFavoriteVacancies()
    }

}
