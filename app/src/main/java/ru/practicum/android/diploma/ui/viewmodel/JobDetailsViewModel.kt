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

    // fun onFavoriteClick() = viewModelScope.launch {
    //    val v = _ui.value.vacancy ?: return@launch
    //    if (v.isFavorite) {
    //        vacanciesInteractor.removeFromFavorites(v.id)
    //        _ui.update { it.copy(vacancy = v.copy(isFavorite = false)) }
    //   } else {
    //        vacanciesInteractor.addToFavorites(v)
    //        _ui.update { it.copy(vacancy = v.copy(isFavorite = true)) }
//    }

}
