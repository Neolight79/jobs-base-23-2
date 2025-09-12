package ru.practicum.android.diploma.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.FilterIndustryResponse
import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(
    private val jobsBaseApiService: JobsBaseApi,
    private val networkConnector: NetworkConnector
) : NetworkClient {

    override suspend fun getVacancies(request: Any?): Response {
        var errorResponse = checkRequest(request)
        if (errorResponse != null) return errorResponse
        return withContext(Dispatchers.IO) {
            val response = runCatching {
                jobsBaseApiService.getVacancies((request as Request).options)
            }.onFailure {
                errorResponse = Response().apply {
                    resultCode = (it as retrofit2.HttpException).response()?.code() ?: HTTP_INTERNAL_SERVER_ERROR_500
                    message = it.response()?.message().toString()
                }
            }
            if (response.isSuccess && response.getOrNull() != null) {
                response.getOrThrow().apply { resultCode = HTTP_OK_200 }
            } else {
                Log.e(ERROR_TAG, (errorResponse as Response).message)
                errorResponse
            }
        }
    }

    override suspend fun getVacancyById(request: Any?): Response {
        var errorResponse = checkRequest(request)
        if (errorResponse != null) return errorResponse
        return withContext(Dispatchers.IO) {
            val response = runCatching {
                val id = (request as Request).options["id"]!!
                jobsBaseApiService.getVacancyById(id)
            }.onFailure {
                errorResponse = Response().apply {
                    resultCode = (it as retrofit2.HttpException).response()?.code() ?: HTTP_INTERNAL_SERVER_ERROR_500
                    message = it.response()?.message().toString()
                }
            }
            if (response.isSuccess) {
                response.getOrThrow().apply { resultCode = HTTP_OK_200 }
            } else {
                Log.e(ERROR_TAG, (errorResponse as Response).message)
                errorResponse
            }
        }
    }

    override suspend fun getAreas(request: Any?): Response {
        var errorResponse = checkRequest(request)
        if (errorResponse != null) return errorResponse
        return withContext(Dispatchers.IO) {
            val response = runCatching {
                jobsBaseApiService.getAreas()
            }.onFailure {
                errorResponse = Response().apply {
                    resultCode = (it as retrofit2.HttpException).response()?.code() ?: HTTP_INTERNAL_SERVER_ERROR_500
                    message = it.response()?.message().toString()
                }
            }
            if (response.isSuccess) {
                response.getOrThrow().apply { resultCode = HTTP_OK_200 }
            } else {
                Log.e(ERROR_TAG, (errorResponse as Response).message)
                errorResponse
            }
        }
    }

    override suspend fun getIndustries(request: Any?): Response {
        val errorResponse = checkRequest(request)
        if (errorResponse != null) return errorResponse

        return withContext(Dispatchers.IO) {
            runCatching { jobsBaseApiService.getIndustries() }
                .fold(
                    onSuccess = { list ->
                        FilterIndustryResponse(results = list).apply {
                            resultCode = HTTP_OK_200
                        }
                    },
                    onFailure = { ex ->
                        Response().apply {
                            resultCode = if (ex is retrofit2.HttpException) {
                                ex.code()
                            } else {
                                HTTP_INTERNAL_SERVER_ERROR_500
                            }
                            message = ex.localizedMessage ?: UNKNOWN_ERROR
                            Log.e(ERROR_TAG, message)
                        }
                    }
                )
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
        const val HTTP_NOT_FOUND_404 = 404
        const val HTTP_INTERNAL_SERVER_ERROR_500 = 500
        const val HTTP_SERVICE_UNAVAILABLE_503 = 503
        const val ERROR_TAG = "Error response message:"
        const val UNKNOWN_ERROR = "Unknown error"
    }
}
