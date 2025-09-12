package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.FilterAreaResponse
import ru.practicum.android.diploma.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.FilterAreaDto
import ru.practicum.android.diploma.data.dto.FilterIndustryResponse
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse

interface JobsBaseApi {

    @GET("/vacancies")
    suspend fun getVacancies(@QueryMap options: Map<String, String>): VacanciesResponse

    @GET("/vacancies/{id}")
    suspend fun getVacancyById(@Path("id") vacancyId: String): VacancyDetailResponse

    @GET("/areas")
    suspend fun getAreas(): List<FilterAreaDto>

    @GET("/industries")
    suspend fun getIndustries(): List<FilterIndustryDto>

}
