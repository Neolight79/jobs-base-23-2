package ru.practicum.android.diploma.domain.models

enum class BottomNavRoutes {
    Main,
    Favorites,
    Team;

    companion object {
        fun isInEnum(someString: String): Boolean {
            return try {
                valueOf(someString)
                true
            } catch (_: Exception) {
                false
            }
        }
    }
}
