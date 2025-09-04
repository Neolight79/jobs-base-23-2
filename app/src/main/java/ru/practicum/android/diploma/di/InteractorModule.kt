package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.FilterParametersInteractorImpl

val interactorModule = module {

    single {
        FilterParametersInteractorImpl(get())
    }

}
