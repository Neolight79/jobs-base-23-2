package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertFavorite(vacancyFavoriteEntity: VacancyFavoriteEntity)

    @Query("DELETE FROM favorites WHERE vacancyId = :vacancyId")
    suspend fun deleteFavorite(vacancyId: String)

    @Query("SELECT * FROM favorites ORDER BY id DESC")
    fun getFavorites(): Flow<List<VacancyFavoriteEntity>>

}
