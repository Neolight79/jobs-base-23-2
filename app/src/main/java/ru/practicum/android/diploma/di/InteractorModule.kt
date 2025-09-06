package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.FilterParametersInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacanciesInteractorImpl

val interactorModule = module {

    single {
        FilterParametersInteractorImpl(get())
    }

    single {
        VacanciesInteractorImpl(get(), get())
    }

}
