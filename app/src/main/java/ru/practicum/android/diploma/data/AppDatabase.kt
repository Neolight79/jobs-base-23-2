package ru.practicum.android.diploma.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.dto.VacancyFavoriteEntity

@Database(version = 1, entities = [VacancyFavoriteEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

}
