package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {

    suspend fun getVacancies(request: Any?): Response

    suspend fun getVacancyById(request: Any?): Response

    suspend fun getAreas(request: Any? = null): Response

    suspend fun getIndustries(request: Any? = null): Response

}
