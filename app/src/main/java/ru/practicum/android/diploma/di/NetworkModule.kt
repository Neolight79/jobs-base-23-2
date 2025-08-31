package ru.practicum.android.diploma.di

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig

val networkModule = module {

    single {
        Interceptor { chain ->
            val token = BuildConfig.API_ACCESS_TOKEN
            val request = chain.request().newBuilder()
                .apply {
                    token.takeIf { it.isNotEmpty() }?.let {
                        header("Authorization", "Bearer $it")
                    }
                }
                .build()

            chain.proceed(request)
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://practicum-diploma-8bc38133faba.herokuapp.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
