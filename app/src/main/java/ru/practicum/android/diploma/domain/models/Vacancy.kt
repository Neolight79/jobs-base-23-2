package ru.practicum.android.diploma.domain.models

data class Vacancy(

    // Идентификатор вакансии
    val id: String,

    // Название вакансии
    val name: String,

    // Описание вакансии
    val description: String,

    // Зарплата
    val salary: String,

    // Город места работы
    val city: String,

    // Полный адрес места работы
    val address: String,

    // Опыт работы
    val experience: String,

    // Режим работы: объединяет employment + schedule
    val conditions: String,

    // Контакты работодателя
    val contact: Contact,

    // Название работодателя
    val employerName: String,

    // URI логотипа работодателя
    val employerLogo: String,

    // Ключевые навыки
    val skills: List<String>,

    // URL вакансии для отправки
    val url: String,

    // Признак того, что вакансия находится в списке избранных
    var isFavorite: Boolean = false

)
