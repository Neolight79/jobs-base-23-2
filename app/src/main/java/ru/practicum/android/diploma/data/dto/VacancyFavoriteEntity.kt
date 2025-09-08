package ru.practicum.android.diploma.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class VacancyFavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vacancyId: String,
    val vacancyName: String?,
    val vacancyDescription: String?,
    val salary: String?,
    val city: String?,
    val address: String?,
    val experience: String?,
    val conditions: String?,
    val contact: String?,
    val employerName: String?,
    val employerLogo: String?,
    val skills: String?,
    val url: String?
)
