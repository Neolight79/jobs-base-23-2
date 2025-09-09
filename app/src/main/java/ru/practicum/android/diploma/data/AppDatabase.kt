package ru.practicum.android.diploma.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.FavoriteDao
import ru.practicum.android.diploma.data.db.VacancyFavoriteEntity

@Database(version = 2, entities = [VacancyFavoriteEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

}
