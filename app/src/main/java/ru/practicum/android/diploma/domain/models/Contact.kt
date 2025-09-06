package ru.practicum.android.diploma.domain.models

data class Contact(
    // Имя контактного лица работодателя
    val name: String,
    // E-mail контактного лица работодателя
    val email: String,
    // Телефоны контактного лица работодателя
    val phones: List<String>
)
