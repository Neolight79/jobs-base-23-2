package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.VacancyFavoriteEntity
import ru.practicum.android.diploma.domain.FavoritesRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.mappers.VacancyDbConverter

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val vacancyDbConvertor: VacancyDbConverter
) : FavoritesRepository {

    override suspend fun addVacancyToFavorites(vacancy: Vacancy) {
        appDatabase.favoriteDao().insertFavorite(vacancyDbConvertor.map(vacancy))
    }

    override suspend fun deleteVacancyFromFavorites(vacancy: Vacancy) {
        appDatabase.favoriteDao().deleteFavorite(vacancyDbConvertor.map(vacancy).vacancyId)
    }

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> = flow {
        val vacancies = appDatabase.favoriteDao().getFavorites().first()
        emit(convertFromVacancyFavoriteEntity(vacancies))
    }

    private fun convertFromVacancyFavoriteEntity(vacancies: List<VacancyFavoriteEntity>): List<Vacancy> {
        return vacancies.map { vacancy -> vacancyDbConvertor.map(vacancy) }
    }

}
