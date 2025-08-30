package ru.practicum.android.diploma.data.dto

data class VacancyDetailDto(
    val id: String,
    val name: String?,
    val description: String?,
    val salary: Salary?,
    val address: Address?,
    val experience: Experience?,
    val schedule: Schedule?,
    val employment: Employment?,
    val contacts: Contacts?,
    val employer: Employer?,
    val area: AreaDto?,
    val skills: List<String>?,
    val url: String?,
    val industry: IndustryDto?,
)

data class Salary (
    val id: String,
    val from: Int?,
    val to: Int?,
    val currency: String?
)

data class Address (
    val city: String?,
    val street: String?,
    val building: String?,
    val fullAddress: String?,
)

data class Experience (
    val id: String,
    val name: String?,
)

data class Schedule (
    val id: String,
    val name: String?
)

data class Employment (
    val id: String,
    val name: String?
)

data class Contacts (
    val id: String,
    val name: String?,
    val email: String?,
    val phones: List<Phone>?
)

data class Phone (
    val comment: String?,
    val formatted: String
)

data class Employer (
    val id: String,
    val name: String?,
    val logo: String?,
)
