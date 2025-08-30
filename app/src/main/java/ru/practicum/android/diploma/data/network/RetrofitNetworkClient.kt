package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(private val jobsBaseApiService: JobsBaseApi,
                            private val networkConnector: NetworkConnector) : NetworkClient {

    override suspend fun getVacancies(request: Any?): Response {
        if (!networkConnector.isConnected()) return Response().apply { resultCode = 503 }
        if (request !is Request) return Response().apply { resultCode = 400 }
        return withContext(Dispatchers.IO) {
            try {
                val response = jobsBaseApiService.getVacancies(request.options)
                response.apply { resultCode = 200 }
            }
            catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    override suspend fun getVacancyById(request: Any?): Response {
        if (!networkConnector.isConnected()) return Response().apply { resultCode = 503 }
        if (request !is Request) return Response().apply { resultCode = 400 }
        return withContext(Dispatchers.IO) {
            try {
                val response = jobsBaseApiService.getVacancyById(request.options["id"])
                response.apply { resultCode = 200 }
            }
            catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    override suspend fun getAreas(request: Any?): Response {
        if (!networkConnector.isConnected()) return Response().apply { resultCode = 503 }
        return withContext(Dispatchers.IO) {
            try {
                val response = jobsBaseApiService.getAreas()
                response.apply { resultCode = 200 }
            }
            catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    override suspend fun getIndustries(request: Any?): Response {
        if (!networkConnector.isConnected()) return Response().apply { resultCode = 503 }
        return withContext(Dispatchers.IO) {
            try {
                val response = jobsBaseApiService.getIndustries()
                response.apply { resultCode = 200 }
            }
            catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

}
