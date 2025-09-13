package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.FavoritesRepositoryImpl
import ru.practicum.android.diploma.data.FilterAreasRepositoryImpl
import ru.practicum.android.diploma.data.FilterParametersRepositoryImpl
import ru.practicum.android.diploma.data.IndustriesRepositoryImpl
import ru.practicum.android.diploma.data.VacanciesRepositoryImpl
import ru.practicum.android.diploma.domain.FavoritesRepository
import ru.practicum.android.diploma.domain.api.FilterAreasRepository
import ru.practicum.android.diploma.domain.api.FilterParametersRepository
import ru.practicum.android.diploma.domain.api.IndustriesRepository
import ru.practicum.android.diploma.domain.api.VacanciesRepository

val repositoryModule = module {

    single<FilterParametersRepository> {
        FilterParametersRepositoryImpl(get(), get())
    }

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(get())
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<FilterAreasRepository> {
        FilterAreasRepositoryImpl(get(), get())
    }

    single<IndustriesRepository> {
        IndustriesRepositoryImpl(get(), get())
    }

}
