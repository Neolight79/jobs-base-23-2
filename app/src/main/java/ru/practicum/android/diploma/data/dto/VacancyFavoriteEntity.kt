package ru.practicum.android.diploma.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class VacancyFavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val vacancyId: String,
    val vacancyName: String?,
    val employerName: String?,
    val employerLogo: String?,
    val salaryFrom: String?,
    val salaryTo: String?,
    val salaryCurrency: String?
)
