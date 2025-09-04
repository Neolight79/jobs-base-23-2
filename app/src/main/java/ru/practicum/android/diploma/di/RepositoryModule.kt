package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.FilterParametersRepositoryImpl

val repositoryModule = module {

    single {
        FilterParametersRepositoryImpl(get(), get())
    }

}
