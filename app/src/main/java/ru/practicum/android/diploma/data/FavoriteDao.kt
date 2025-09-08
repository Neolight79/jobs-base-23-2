package ru.practicum.android.diploma.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.VacancyFavoriteEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(vacancyFavoriteEntity: VacancyFavoriteEntity)

    @Query("DELETE FROM favorites WHERE vacancyId = :vacancyId")
    suspend fun deleteFavorite(vacancyId: String)

    @Query("DELETE FROM favorites WHERE vacancyId = :vacancyId")
    suspend fun deleteByVacancyId(vacancyId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE vacancyId = :vacancyId)")
    suspend fun isFavorite(vacancyId: String): Boolean

    @Query("SELECT vacancyId FROM favorites")
    suspend fun getFavoriteIdsOnce(): List<String>

    @Query("SELECT * FROM favorites ORDER BY id DESC")
    fun getFavorites(): Flow<List<VacancyFavoriteEntity>>
}
