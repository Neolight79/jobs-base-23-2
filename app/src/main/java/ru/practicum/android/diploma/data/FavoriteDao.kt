package ru.practicum.android.diploma.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.VacancyFavoriteEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(vacancyFavoriteEntity: VacancyFavoriteEntity)

    @Delete
    suspend fun deleteFavorite(vacancyFavoriteEntity: VacancyFavoriteEntity)

    @Query("SELECT * FROM favorites ORDER BY id DESC")
    fun getFavorites(): Flow<List<VacancyFavoriteEntity>>

}
