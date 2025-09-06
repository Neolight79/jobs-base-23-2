package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.FilterParametersRepositoryImpl
import ru.practicum.android.diploma.data.VacanciesRepositoryImpl
import ru.practicum.android.diploma.domain.api.FilterParametersRepository
import ru.practicum.android.diploma.domain.api.VacanciesRepository

val repositoryModule = module {

    single<FilterParametersRepository> {
        FilterParametersRepositoryImpl(get(), get())
    }

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(get())
    }
}
