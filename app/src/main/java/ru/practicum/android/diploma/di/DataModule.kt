package ru.practicum.android.diploma.di

import android.content.res.Resources
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    factory<Gson> { Gson() }

    single<Resources> {
        androidContext().resources
    }

}
