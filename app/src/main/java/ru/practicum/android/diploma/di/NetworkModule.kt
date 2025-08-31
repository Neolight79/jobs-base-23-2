package ru.practicum.android.diploma.di

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.JobsBaseApi
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.NetworkConnector
import ru.practicum.android.diploma.data.network.NetworkConnectorImpl
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

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
            .create(JobsBaseApi::class.java)
    }

    single<NetworkConnector> {
        NetworkConnectorImpl(androidContext())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

}
