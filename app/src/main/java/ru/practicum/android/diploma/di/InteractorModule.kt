package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.impl.FilterParametersInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacanciesInteractorImpl

val interactorModule = module {

    single<FilterParametersInteractor> {
        FilterParametersInteractorImpl(get())
    }

    single<VacanciesInteractor> {
        VacanciesInteractorImpl(get(), get(), get())
    }

}
