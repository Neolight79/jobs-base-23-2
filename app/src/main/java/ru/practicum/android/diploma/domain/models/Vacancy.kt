package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: Integer,
    val name: String,
    val description: String,
    val salary: Salary,
    val address: Address,
    val experience: Experience,
    val schedule: Schedule,
    val employment: Employment,
    val contacts: Contacts,
    val employer: Employer,
    val area: FilterArea,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustry,
)
