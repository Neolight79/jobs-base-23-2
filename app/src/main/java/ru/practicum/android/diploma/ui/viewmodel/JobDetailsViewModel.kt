package ru.practicum.android.diploma.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.SearchResultStatus
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyState

class JobDetailsViewModel(
    jobID: String,
    private val vacanciesInteractor: VacanciesInteractor,
    private val application: Application
) : ViewModel() {

    // Временный StateFlow для передачи идентификатора вакансии
    private val _vacancyIdState = MutableStateFlow<String>(jobID)
    val vacancyIdState: StateFlow<String> = _vacancyIdState.asStateFlow()

    private val _vacancyState = MutableStateFlow<VacancyState>(VacancyState.Loading)
    val vacancyState: StateFlow<VacancyState> = _vacancyState.asStateFlow()

    fun getVacancyDetail(vacancyId: String) {
        renderState(VacancyState.Loading)
        viewModelScope.launch {
            val response = vacanciesInteractor.getVacancyById(vacancyId)
            processResult(response.first, response.second)
        }
    }


    private fun processResult(vacancy: Vacancy?, searchStatus: SearchResultStatus) {
        when (searchStatus) {
            SearchResultStatus.Success -> {
                if (vacancy == null) {
                    renderState(VacancyState.EmptyResult)
                } else {
                    renderState(VacancyState.VacancyDetail(vacancy))
                }
            }
            else -> renderState(VacancyState.ServerError)
        }


    }

    private fun renderState(state: VacancyState) {
        _vacancyState.value = state
    }

}
