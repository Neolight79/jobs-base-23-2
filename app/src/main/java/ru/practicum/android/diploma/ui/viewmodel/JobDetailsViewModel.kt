package ru.practicum.android.diploma.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.domain.api.VacanciesInteractor

class JobDetailsViewModel(
    jobID: String,
    private val vacanciesInteractor: VacanciesInteractor,
    private val application: Application
) : ViewModel() {

    // Временный StateFlow для передачи идентификатора вакансии
    private val _vacancyIdState = MutableStateFlow<String>(jobID)
    val vacancyIdState: StateFlow<String> = _vacancyIdState.asStateFlow()

}
