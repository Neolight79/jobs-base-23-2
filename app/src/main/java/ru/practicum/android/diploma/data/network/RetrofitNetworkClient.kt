package ru.practicum.android.diploma.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(
    private val jobsBaseApiService: JobsBaseApi,
    private val networkConnector: NetworkConnector
) : NetworkClient {

    override suspend fun getVacancies(request: Any?): Response {
        val errorResponse = checkRequest(request)
        if (errorResponse != null) return errorResponse
        return withContext(Dispatchers.IO) {
            val response = runCatching {
                jobsBaseApiService.getVacancies((request as Request).options)
            }
            if (response.isSuccess && response.getOrNull() != null) {
                response.getOrThrow().apply { resultCode = HTTP_OK_200 }
            } else {
                Response().apply {
                    resultCode = HTTP_INTERNAL_SERVER_ERROR_500
                    Log.e(ERROR_TAG, "$ERROR_MESSAGE $message")
                }
            }
        }
    }

    override suspend fun getVacancyById(request: Any?): Response {
        val errorResponse = checkRequest(request)
        if (errorResponse != null) return errorResponse
        return withContext(Dispatchers.IO) {
            val response = runCatching {
                val id = (request as Request).options["id"]!!
                jobsBaseApiService.getVacancyById(id)
            }
            if (response.isSuccess && response.getOrNull() != null) {
                response.getOrThrow().apply { resultCode = HTTP_OK_200 }
            } else {
                Response().apply {
                    resultCode = HTTP_INTERNAL_SERVER_ERROR_500
                    Log.e(ERROR_TAG, "$ERROR_MESSAGE $message")
                }
            }
        }
    }

    override suspend fun getAreas(request: Any?): Response {
        val errorResponse = checkRequest(request)
        if (errorResponse != null) return errorResponse
        return withContext(Dispatchers.IO) {
            val response = runCatching {
                jobsBaseApiService.getAreas()
            }
            if (response.isSuccess && response.getOrNull() != null) {
                response.getOrThrow().apply { resultCode = HTTP_OK_200 }
            } else {
                Response().apply {
                    resultCode = HTTP_INTERNAL_SERVER_ERROR_500
                    Log.e(ERROR_TAG, "$ERROR_MESSAGE $message")
                }
            }
        }
    }

    override suspend fun getIndustries(request: Any?): Response {
        val errorResponse = checkRequest(request)
        if (errorResponse != null) return errorResponse
        return withContext(Dispatchers.IO) {
            val response = runCatching {
                jobsBaseApiService.getIndustries()
            }
            if (response.isSuccess && response.getOrNull() != null) {
                response.getOrThrow().apply { resultCode = HTTP_OK_200 }
            } else {
                Response().apply {
                    resultCode = HTTP_INTERNAL_SERVER_ERROR_500
                    Log.e(ERROR_TAG, "$ERROR_MESSAGE $message")
                }
            }
        }
    }

    private fun checkRequest(request: Any?): Response? {
        val response = Response()
        if (!networkConnector.isConnected()) response.apply { resultCode = HTTP_SERVICE_UNAVAILABLE_503 }
        if (request != null && request !is Request) response.apply { resultCode = HTTP_BAD_REQUEST_400 }
        return if (response.resultCode == 0) {
            null
        } else {
            response
        }
    }

    companion object {
        const val HTTP_OK_200 = 200
        const val HTTP_BAD_REQUEST_400 = 400
        const val HTTP_INTERNAL_SERVER_ERROR_500 = 500
        const val HTTP_SERVICE_UNAVAILABLE_503 = 503
        const val ERROR_MESSAGE = "Error message:"
        const val ERROR_TAG = "Error response"
    }
}
