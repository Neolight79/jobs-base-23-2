package ru.practicum.android.diploma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.SearchResultStatus
import ru.practicum.android.diploma.domain.models.SearchState
import ru.practicum.android.diploma.domain.models.VacanciesPage
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.debounce

class MainViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {

    // Общие переменные
    private var latestSearchText = ""
    private var isDirectSearchRun = false

    // Переменные для постраничной обработки
    private val vacanciesList: MutableList<Vacancy> = mutableListOf()
    private var currentPage: Int = 0
    private var totalVacancies: Int = 0

    // StateFlow для состояния экрана поиска вакансий
    private val _searchState = MutableStateFlow<SearchState>(SearchState.EmptyString)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    // StateFlow для строки поиска
    private val _searchTextState = MutableStateFlow(latestSearchText)
    val searchTextState: StateFlow<String> = _searchTextState.asStateFlow()

    // StateFlow для признака заполненных фильтров
    private val _filtersEnabledState = MutableStateFlow(false)
    val filtersEnabledState: StateFlow<Boolean> = _filtersEnabledState.asStateFlow()

    // StateFlow для сокрытия клавиатуры
    private val _hideKeyboardState = MutableSharedFlow<Unit>(replay = 0)
    val hideKeyboardState = _hideKeyboardState.asSharedFlow()

    // Процедура запуска отложенного поиска
    private val vacancySearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY_MILLIS,
        viewModelScope,
        true
    ) { changedText ->
        if (!isDirectSearchRun) search(changedText)
    }

    // Доступные методы
    fun searchDirectly() {
        initPagingVariables()
        search(latestSearchText)
        isDirectSearchRun = true
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            _searchTextState.value = changedText
            when (changedText.isEmpty()) {
                true -> clearSearch()
                false -> {
                    initPagingVariables()
                    vacancySearchDebounce(changedText)
                    isDirectSearchRun = false
                    renderState(SearchState.InputInProgress)
                }
            }
        }
    }

    fun loadNextPage() {
        if (vacanciesList.size < totalVacancies) {
            search(latestSearchText)
        }
    }

    fun hideKeyboard() {
        viewModelScope.launch {
            _hideKeyboardState.emit(Unit)
        }
    }

    // Приватные методы
    private fun initPagingVariables() {
        vacanciesList.clear()
        currentPage = 0
        totalVacancies = 0
    }

    private fun search(newSearchText: String) {
        if (newSearchText != latestSearchText) return
        if (newSearchText.isNotEmpty()) {
            if (currentPage == 0) renderState(SearchState.Loading)

            // Скрываем клавиатуру
            hideKeyboard()

            viewModelScope.launch {
                val response = vacanciesInteractor.searchVacancies(text = newSearchText, page = currentPage + 1)
                processResult(response.first, response.second)
            }

        } else {
            clearSearch()
        }
    }

    private fun processResult(foundVacancies: VacanciesPage?, searchStatus: SearchResultStatus) {
        if (currentPage == 0) {
            when (searchStatus) {
                SearchResultStatus.Success -> {
                    if (foundVacancies == null) {
                        renderState(SearchState.EmptyResult)
                    } else {
                        renderState(provideVacancies(foundVacancies))
                    }
                }
                SearchResultStatus.NoConnection -> {
                    renderState(SearchState.NoConnection)
                }
                SearchResultStatus.ServerError -> {
                    renderState(SearchState.ServerError)
                }
            }
        } else {
            when (searchStatus) {
                SearchResultStatus.Success -> {
                    if (foundVacancies == null) {
                        renderState(provideEndOfList())
                    } else {
                        renderState(provideVacancies(foundVacancies))
                    }
                }
                else -> renderState(provideEndOfList())
            }
        }
    }

    private fun provideVacancies(foundVacancies: VacanciesPage): SearchState {
        if (foundVacancies.pageNumber == currentPage + 1) {
            currentPage += 1
            totalVacancies = foundVacancies.totalVacancies
            vacanciesList.addAll(foundVacancies.vacancies)
        }
        return SearchState.VacanciesFound(
            isShowTrailingPlaceholder = vacanciesList.size < totalVacancies,
            vacanciesQuantity = totalVacancies,
            vacanciesList = vacanciesList.toList()
        )
    }

    private fun provideEndOfList(): SearchState {
        return SearchState.VacanciesFound(
            isShowTrailingPlaceholder = false,
            vacanciesQuantity = totalVacancies,
            vacanciesList = vacanciesList.toList()
        )
    }

    private fun renderState(state: SearchState) {
        _searchState.value = state
    }

    fun clearSearch() {
        latestSearchText = ""
        _searchTextState.value = ""
        initPagingVariables()
        renderState(SearchState.EmptyString)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }

}
