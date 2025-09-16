package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.viewmodel.FavoritesViewModel
import ru.practicum.android.diploma.ui.viewmodel.FilterIndustryViewModel
import ru.practicum.android.diploma.ui.viewmodel.FilterLocationCountryViewModel
import ru.practicum.android.diploma.ui.viewmodel.FilterLocationRegionViewModel
import ru.practicum.android.diploma.ui.viewmodel.FilterLocationViewModel
import ru.practicum.android.diploma.ui.viewmodel.FiltersViewModel
import ru.practicum.android.diploma.ui.viewmodel.JobDetailsViewModel
import ru.practicum.android.diploma.ui.viewmodel.MainViewModel

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), get(), get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        FiltersViewModel(get())
    }

    viewModel {
        FilterLocationViewModel(get())
    }

    viewModel {
        FilterLocationCountryViewModel(get())
    }

    viewModel {
        FilterIndustryViewModel(get(), get())
    }

    viewModel { params ->
        FilterLocationRegionViewModel(params.get(), get())
    }

    viewModel { params ->
        JobDetailsViewModel(params.get(), get(), get(), get())
    }
}
