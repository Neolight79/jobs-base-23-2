package ru.practicum.android.diploma.domain.models

sealed interface VacancyState {

    data object Loading : VacancyState

    data object EmptyResult : VacancyState

    data object ServerError : VacancyState

    data class VacancyDetail(val vacancy: Vacancy) : VacancyState

}
