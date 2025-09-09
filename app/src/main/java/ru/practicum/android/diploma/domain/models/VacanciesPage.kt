package ru.practicum.android.diploma.domain.models

data class VacanciesPage(

    // Количество вакансий в запросе
    val totalVacancies: Int,

    // Номер текущей страницы
    val pageNumber: Int,

    // Вакансии на текущей странице
    val vacancies: List<Vacancy>

)
