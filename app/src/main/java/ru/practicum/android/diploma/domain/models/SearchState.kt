package ru.practicum.android.diploma.domain.models

sealed interface SearchState {

    data object EmptyString : SearchState

    data object InputInProgress : SearchState

    data object Loading : SearchState

    data object NoConnection : SearchState

    data object ServerError : SearchState

    data object EmptyResult : SearchState

    data class VacanciesFound(
        val isShowTrailingPlaceholder: Boolean,
        val errorMessage: String? = null,
        val vacanciesQuantity: Int,
        val vacanciesList: List<Vacancy>
    ) : SearchState

}
