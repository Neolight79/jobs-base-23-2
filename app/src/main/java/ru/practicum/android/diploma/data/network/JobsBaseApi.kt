package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.AreaResponse
import ru.practicum.android.diploma.data.dto.IndustryResponse
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse

interface JobsBaseApi {

    @GET("/vacancies")
    suspend fun getVacancies(@QueryMap options: Map<String, String>) : VacanciesResponse

    @GET("/vacancies/{id}")
    suspend fun getVacancyById(@Path("id") vacancyId: String?) : VacancyDetailResponse

    @GET("/areas")
    suspend fun getAreas() : AreaResponse

    @GET("/industries")
    suspend fun getIndustries() : IndustryResponse

}
