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

class JobDetailsViewModel(
    jobID: String,
    private val vacanciesInteractor: VacanciesInteractor,
    private val application: Application
) : ViewModel() {

    // Временный StateFlow для передачи идентификатора вакансии
    private val _vacancyIdState = MutableStateFlow<String>(jobID)
    val vacancyIdState: StateFlow<String> = _vacancyIdState.asStateFlow()

    fun getVacancyDetail(vacancyId: String) {
        viewModelScope.launch {
            val response = vacanciesInteractor.getVacancyById(vacancyId)
            processResult(response.first, response.second)
        }
    }

    private fun processResult(foundVacancy: Vacancy?, searchStatus: SearchResultStatus) {
        val context = application.applicationContext
        Log.d("MY DEBUG", foundVacancy.toString())
    }
}
