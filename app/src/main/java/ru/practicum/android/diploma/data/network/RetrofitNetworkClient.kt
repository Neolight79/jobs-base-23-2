package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(
    private val jobsBaseApiService: JobsBaseApi,
    private val networkConnector: NetworkConnector
) : NetworkClient {

    override suspend fun getVacancies(request: Any?): Response {
        val errorResponse = Response()
        if (!networkConnector.isConnected()) errorResponse.apply { resultCode = HTTP_SERVICE_UNAVAILABLE_503 }
        if (request !is Request) errorResponse.apply { resultCode = HTTP_BAD_REQUEST_400 }
        if (errorResponse.resultCode != 0) return errorResponse
        return withContext(Dispatchers.IO) {
            val response = jobsBaseApiService.getVacancies((request as Request).options)
            response.apply { resultCode = HTTP_OK_200 }
            /* Необходимо реализовать обработку ошибок иным способом. Detekt ругается на обработку исключений
            try {
                val response = jobsBaseApiService.getVacancies((request as Request).options)
                response.apply { resultCode = HTTP_OK_200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = HTTP_INTERNAL_SERVER_ERROR_500 }
            }
             */
        }
    }

    override suspend fun getVacancyById(request: Any?): Response {
        val errorResponse = Response()
        if (!networkConnector.isConnected()) errorResponse.apply { resultCode = HTTP_SERVICE_UNAVAILABLE_503 }
        if (request !is Request) errorResponse.apply { resultCode = HTTP_BAD_REQUEST_400 }
        if (errorResponse.resultCode != 0) return errorResponse
        return withContext(Dispatchers.IO) {
            val response = jobsBaseApiService.getVacancyById((request as Request).options["id"])
            response.apply { resultCode = HTTP_OK_200 }
        }
    }

    override suspend fun getAreas(request: Any?): Response {
        if (!networkConnector.isConnected()) return Response().apply { resultCode = HTTP_SERVICE_UNAVAILABLE_503 }
        return withContext(Dispatchers.IO) {
            val response = jobsBaseApiService.getAreas()
            response.apply { resultCode = HTTP_OK_200 }
        }
    }

    override suspend fun getIndustries(request: Any?): Response {
        if (!networkConnector.isConnected()) return Response().apply { resultCode = HTTP_SERVICE_UNAVAILABLE_503 }
        return withContext(Dispatchers.IO) {
            val response = jobsBaseApiService.getAreas()
            response.apply { resultCode = HTTP_OK_200 }
        }
    }

    companion object {
        const val HTTP_OK_200 = 200
        const val HTTP_BAD_REQUEST_400 = 400
        const val HTTP_INTERNAL_SERVER_ERROR_500 = 500
        const val HTTP_SERVICE_UNAVAILABLE_503 = 503
    }
}
