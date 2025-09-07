package ru.practicum.android.diploma.di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.AppDatabase
import ru.practicum.android.diploma.data.FilterParametersSharedStorageImpl
import ru.practicum.android.diploma.data.SharedStorage
import ru.practicum.android.diploma.util.mappers.FilterAreaMapper
import ru.practicum.android.diploma.util.mappers.FilterIndustryMapper
import ru.practicum.android.diploma.util.mappers.FilterParametersMapper
import ru.practicum.android.diploma.util.mappers.VacancyMapper

private const val SHARED_PREFERENCES_FILE_NAME = "favorites_shared_preferences"
private const val DB_NAME = "database.db"

val dataModule = module {

    factory<Gson> { Gson() }

    single<Resources> {
        androidContext().resources
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DB_NAME)
            .build()
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE)
    }

    single<SharedStorage> {
        FilterParametersSharedStorageImpl(get(), get())
    }

    single {
        FilterAreaMapper()
    }

    single {
        FilterIndustryMapper()
    }

    single {
        FilterParametersMapper(get(), get())
    }

    single {
        VacancyMapper(
            filterAreaMapper = get(),
            filterIndustryMapper = get()
        )
    }

}
