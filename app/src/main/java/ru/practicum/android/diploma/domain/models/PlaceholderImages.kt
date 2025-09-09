package ru.practicum.android.diploma.domain.models

import androidx.annotation.DrawableRes
import ru.practicum.android.diploma.R

enum class PlaceholderImages(@DrawableRes val imageResId: Int) {

    // Стандартный плейсхолдер для фона экрана поиска
    EmptySearchString(R.drawable.ph_search),

    // Плейсхолдер для ошибки связи и отсутствия интернета
    NoConnection(R.drawable.ph_no_connection),

    // Плейсхолдер, если ни одна вакансия не найдена
    EmptyJobList(R.drawable.ph_empty_list),

    // Плейсхолдер, если регион не найден по контекстному поиску
    NoLocationFound(R.drawable.ph_empty_list),

    // Плейсхолдер, если произошла ошибка API при запросе списка регионов
    LocationsApiError(R.drawable.ph_locations_list_error),

    // Плейсхолдер, если вакансия была удалена с сервера
    JobDeleted(R.drawable.ph_job_deleted),

    // Плейсхолдер при ошибке сервера при поиске вакансии
    SearchServerError(R.drawable.ph_search_server_error),

    // Плейсхолдер при ошибке сервера при загрузке деталей по вакансии
    JobDetailsServerError(R.drawable.ph_job_details_server_error),

    // Плейсхолдер при пустом списке избранного
    FavoritesEmptyList(R.drawable.ph_empty_favorites),

    // Плейсхолдер при ошибке считывания избранного из базы данных
    FavoritesError(R.drawable.ph_empty_list)

}
