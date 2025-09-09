package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.viewmodel.JobDetailsViewModel
import ru.practicum.android.diploma.ui.viewmodel.MainViewModel

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), get(), get())
    }

    viewModel { params ->
        JobDetailsViewModel(params.get(), get(), get(), get())
    }
}
