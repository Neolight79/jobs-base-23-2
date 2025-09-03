package ru.practicum.android.diploma.data

data class TeamMember(
    val name: String,
    val surname: String,
    val currentJob: String?,
    val githubName: String?,
    val imageRes: Int? = null,
)
