package ru.practicum.android.diploma.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.FavoritesInteractor
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.SearchResultStatus
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyState

class JobDetailsViewModel(
    private val jobID: String,
    private val vacanciesInteractor: VacanciesInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val application: Application
) : ViewModel() {

    private val _vacancyState = MutableStateFlow<VacancyState>(VacancyState.Loading)
    val vacancyState: StateFlow<VacancyState> = _vacancyState.asStateFlow()

    private val _favoriteState = MutableStateFlow<Boolean>(false)
    val favoriteState: StateFlow<Boolean> = _favoriteState.asStateFlow()

    private lateinit var currentVacancy: Vacancy

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (_favoriteState.value) {
                favoritesInteractor.deleteVacancyFromFavorites(currentVacancy)
            } else {
                favoritesInteractor.addVacancyToFavorites(currentVacancy)
            }
        }
        renderFavoriteState(!_favoriteState.value)
    }

    fun onShareClicked(vacancy: Vacancy) {
        makeIntent(
            action = Intent.ACTION_SEND,
            extra = vacancy.url,
            type = "text/plain"
        )
    }

    fun requestVacancyDetail() {
        renderState(VacancyState.Loading)
        viewModelScope.launch {
            val response = vacanciesInteractor.getVacancyById(jobID)
            processResult(response.first, response.second)
        }
    }

    fun sendEmail(mailTo: String) {
        makeIntent(Intent.ACTION_SENDTO, "mailto: $mailTo".toUri())
    }

    fun makeCall(callTo: String) {
        makeIntent(Intent.ACTION_DIAL, "tel: $callTo".toUri())
    }

    private fun makeIntent(
        action: String,
        data: Uri? = null,
        extra: String? = null,
        type: String? = null
    ) {
        val context = application.applicationContext
        val intent = Intent(action)
        if (data != null) intent.data = data
        if (extra != null) intent.putExtra(Intent.EXTRA_TEXT, extra)
        if (type != null) intent.type = type
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val chooser = Intent.createChooser(intent, context.getString(R.string.choose_app))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    private fun processResult(vacancy: Vacancy?, searchStatus: SearchResultStatus) {
        when (searchStatus) {
            SearchResultStatus.Success -> {
                if (vacancy == null) {
                    renderState(VacancyState.EmptyResult)
                } else {
                    currentVacancy = vacancy
                    renderState(VacancyState.VacancyDetail(vacancy))
                    renderFavoriteState(vacancy.isFavorite)
                }
            }
            SearchResultStatus.NoConnection -> {
                renderState(VacancyState.ServerError)
            }
            else -> renderState(VacancyState.EmptyResult)
        }
    }

    private fun renderFavoriteState(isFavorite: Boolean) {
        _favoriteState.value = isFavorite
    }

    private fun renderState(state: VacancyState) {
        _vacancyState.value = state
    }

}
