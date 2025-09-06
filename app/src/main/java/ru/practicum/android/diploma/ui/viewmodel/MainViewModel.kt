package ru.practicum.android.diploma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.domain.models.Contact
import ru.practicum.android.diploma.domain.models.SearchResultStatus
import ru.practicum.android.diploma.domain.models.SearchState
import ru.practicum.android.diploma.domain.models.VacanciesPage
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.mappers.VacancyMapper
import kotlin.random.Random

class MainViewModel(networkClient: NetworkClient, vacancyMapper: VacancyMapper) : ViewModel() {

    val interactor = VacanciesInteractorImpl(networkClient, vacancyMapper)

    // Моковый результат поиска ToDo удалить!
    val vacanciesSearchResultsList = List(Random.nextInt(80, 100)) { index ->
        mockVacationsSourceList.random().copy(
            employerName = index.plus(1).toString()
        )
    }

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
        if (newSearchText.isNotEmpty()) {
            if (currentPage == 0) renderState(SearchState.Loading)

            // Скрываем клавиатуру
            hideKeyboard()

//            viewModelScope.launch {
//                interactor.searchVacancies(
//                    text = newSearchText,
//                    onlyWithSalary = false,
//                    page = currentPage
//                ).collect { favoriteTracks ->
//                    if (favoriteTracks.isEmpty())
//                        renderState(FavoriteState.Empty)
//                    else
//                        renderState(FavoriteState.TracksFavorite(favoriteTracks))
//                }
//            }

            // ToDo запускаем в интеракторе функцию поиска вакансий
            // ToDo но до подключения интерактора будем использовать моковые методы в текущем классе
            viewModelScope.launch {
                try {
                    val a = interactor.searchVacancies(
                        text = newSearchText,
                        onlyWithSalary = false,
                        page = currentPage
                    )
                    val d = a
                } catch (e: Exception) {
                    val b = 0
                }
//                searchMockVacancies(newSearchText, currentPage + 1).collect { pair ->
//                    processResult(pair.first, pair.second)
//                }
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

        // ToDo далее следуют моковые данные для тестирования, удалить
        const val VACANCIES_PER_PAGE = 20
        private val mockVacationsSourceList = listOf(
            Vacancy(
                id = "da8c8786-6d06-4a64-be1c-23ea2d70fd7d",
                name = "iOS Developer в Google",
                description = "Обязанности:\n\nРабота над проектами в составе команды бекенд, фронтенд разработчиков и дизайнеров интерфейсов.\nПриоритетная задача реализовать запланированное мобильное приложение.\nТребования:\n\nУмение работать с основными UI компонентами, опыт реализации кастомных View;\nОпыт работы с HTTP(S), REST API;\nОпыт работы с Async/Await;\nПонимание чистой архитектуры и основных паттернов проектирования;\nПонимание принципов SOLID;\nОпыт работы с Git, Cocoapods.\nЗнания Swift и UIKit;\nУмение писать UI кодом без использования Storyboard;\nЗнание Apple Human Interface Guidlines;\nУмение работать в команде, способность принимать самостоятельные решения;\nКреативность, творческое мышление.",
                salary = "Зарплата не указана",
                city = "Саратов",
                address = "Саратов, Ленина, 18",
                experience = "Нет опыта",
                conditions = "Полная занятость, Полный день",
                contact = Contact(
                    name = "Кузнецов Сергей Петрович",
                    email = "",
                    phones = listOf("+7 (999) 567-89-01", "+7 (999) 543-21-09")
                ),
                employerName = "Google",
                employerLogo = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Google_2015_logo.svg/1024px-Google_2015_logo.svg.png",
                skills = listOf("Kotlin", "JavaScript", "Swift", "HTML", "Python"),
                url = "cek6h0n4ffe7ur.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com/vacancies/da8c8786-6d06-4a64-be1c-23ea2d70fd7d",
                isFavorite = false
            ),
            Vacancy(
                id = "af7dd6b8-2367-4695-82df-3470717cee2a",
                name = "Android Developer в Microsoft",
                description = "Задачи, которые могут стать твоими:\n\nРазработка новой функциональности мобильного приложения под Android, его архитектуры и исправление существующих недостатков;\nНаписание качественного, чистого, читаемого кода, code-review;\nРазработка общих архитектурных решений;\nВзаимодействие с менеджерами, дизайнерами, бекендерами, тестировщиками;\nПроактивно участвовать в жизни продукта: обсуждении требований, планировании проектов, проектировании дизайна, прототипов, спецификаций;\nДелиться технической экспертизой: предлагать, обсуждать и интегрировать новые решения;\nДекомпозировать, оценивать сроки реализации задач и выдерживать их;\nПроектировать клиент-серверное взаимодействие;\nРазбираться в чужом коде и проводить его рефакторинг;\nДоносить свои мысли и отстаивать свою точку зрения перед остальными членами команды;\nНе просто накидывать идеи, а реализовывать и доводить их до конца в общем проекте;\n\nЧто нужно знать:\n\nAndroid SDK, Android Support Libraries\nПаттерны построения мобильного UI/UX, принципы Material Desig\nПаттерны проектирования, ООП, SOLID, понимание функционального реактивного кода, Clean; Architecture\nKotlin\nDagger, Kotlin Coroutines, Kotlin Flow, Compose, MVVM / MVI, Room\nGradle Multi Modules\nЗнание архитектуры OS Android и особенностей его версий 21+\nВладение техническим английским языком на уровне чтения и понимания\n\nБудет плюсом:\n\nОпыт RxJava\nОпыт написания Unit и UI тестов\nОпыт в Backend Driven UI подходе\nРабота с Gradle\nОпыт работы с CI&DI",
                salary = "от 50 000 до 100 000 ₽",
                city = "Пермь",
                address = "Пермь, Ленина, 13",
                experience = "Нет опыта",
                conditions = "Полная занятость, Полный день",
                contact = Contact(
                    name = "Петров Петр Петрович",
                    email = "",
                    phones = listOf("+7 (999) 234-56-78", "+7 (999) 876-54-32")
                ),
                employerName = "Microsoft",
                employerLogo = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/1200px-Microsoft_logo.svg.png",
                skills = listOf("CSS", "Swift", "HTML", "Go", "C#"),
                url = "cek6h0n4ffe7ur.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com/vacancies/af7dd6b8-2367-4695-82df-3470717cee2a",
                isFavorite = false
            ),
            Vacancy(
                id = "f8ed866f-bf18-4b38-ad9d-258bc0f720ef",
                name = "Android Developer в Apple",
                description = "Задачи, которые могут стать твоими:\n\nРазработка новой функциональности мобильного приложения под Android, его архитектуры и исправление существующих недостатков;\nНаписание качественного, чистого, читаемого кода, code-review;\nРазработка общих архитектурных решений;\nВзаимодействие с менеджерами, дизайнерами, бекендерами, тестировщиками;\nПроактивно участвовать в жизни продукта: обсуждении требований, планировании проектов, проектировании дизайна, прототипов, спецификаций;\nДелиться технической экспертизой: предлагать, обсуждать и интегрировать новые решения;\nДекомпозировать, оценивать сроки реализации задач и выдерживать их;\nПроектировать клиент-серверное взаимодействие;\nРазбираться в чужом коде и проводить его рефакторинг;\nДоносить свои мысли и отстаивать свою точку зрения перед остальными членами команды;\nНе просто накидывать идеи, а реализовывать и доводить их до конца в общем проекте;\n\nЧто нужно знать:\n\nAndroid SDK, Android Support Libraries\nПаттерны построения мобильного UI/UX, принципы Material Desig\nПаттерны проектирования, ООП, SOLID, понимание функционального реактивного кода, Clean; Architecture\nKotlin\nDagger, Kotlin Coroutines, Kotlin Flow, Compose, MVVM / MVI, Room\nGradle Multi Modules\nЗнание архитектуры OS Android и особенностей его версий 21+\nВладение техническим английским языком на уровне чтения и понимания\n\nБудет плюсом:\n\nОпыт RxJava\nОпыт написания Unit и UI тестов\nОпыт в Backend Driven UI подходе\nРабота с Gradle\nОпыт работы с CI&DI",
                salary = "от 1 500 до 2 500 €",
                city = "Саратов",
                address = "Саратов, Ленина, 18",
                experience = "Нет опыта",
                conditions = "Полная занятость, Полный день",
                contact = Contact(
                    name = "Кузнецов Сергей Петрович",
                    email = "",
                    phones = listOf("+7 (999) 567-89-01", "+7 (999) 543-21-09")
                ),
                employerName = "Apple",
                employerLogo = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/1200px-Apple_logo_black.svg.png",
                skills = listOf("SQL", "JavaScript", "Ruby", "Java", "HTML"),
                url = "cek6h0n4ffe7ur.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com/vacancies/f8ed866f-bf18-4b38-ad9d-258bc0f720ef",
                isFavorite = false
            ),
            Vacancy(
                id = "0fbdb9fc-c3d0-43f5-a9b9-d016087a6fa6",
                name = "Android Developer в Яндекс",
                description = "Задачи, которые могут стать твоими:\n\nРазработка новой функциональности мобильного приложения под Android, его архитектуры и исправление существующих недостатков;\nНаписание качественного, чистого, читаемого кода, code-review;\nРазработка общих архитектурных решений;\nВзаимодействие с менеджерами, дизайнерами, бекендерами, тестировщиками;\nПроактивно участвовать в жизни продукта: обсуждении требований, планировании проектов, проектировании дизайна, прототипов, спецификаций;\nДелиться технической экспертизой: предлагать, обсуждать и интегрировать новые решения;\nДекомпозировать, оценивать сроки реализации задач и выдерживать их;\nПроектировать клиент-серверное взаимодействие;\nРазбираться в чужом коде и проводить его рефакторинг;\nДоносить свои мысли и отстаивать свою точку зрения перед остальными членами команды;\nНе просто накидывать идеи, а реализовывать и доводить их до конца в общем проекте;\n\nЧто нужно знать:\n\nAndroid SDK, Android Support Libraries\nПаттерны построения мобильного UI/UX, принципы Material Desig\nПаттерны проектирования, ООП, SOLID, понимание функционального реактивного кода, Clean; Architecture\nKotlin\nDagger, Kotlin Coroutines, Kotlin Flow, Compose, MVVM / MVI, Room\nGradle Multi Modules\nЗнание архитектуры OS Android и особенностей его версий 21+\nВладение техническим английским языком на уровне чтения и понимания\n\nБудет плюсом:\n\nОпыт RxJava\nОпыт написания Unit и UI тестов\nОпыт в Backend Driven UI подходе\nРабота с Gradle\nОпыт работы с CI&DI",
                salary = "Зарплата не указана",
                city = "Москва",
                address = "Москва, Тверская, 1",
                experience = "Нет опыта",
                conditions = "Полная занятость, Полный день",
                contact = Contact(
                    name = "Кузнецов Сергей Петрович",
                    email = "",
                    phones = listOf("+7 (999) 567-89-01", "+7 (999) 543-21-09")
                ),
                employerName = "Яндекс",
                employerLogo = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f1/Yandex_logo_2021_Russian.svg/1024px-Yandex_logo_2021_Russian.svg.png",
                skills = listOf("NoSQL", "Swift", "Ruby", "Kotlin", "Go"),
                url = "cek6h0n4ffe7ur.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com/vacancies/0fbdb9fc-c3d0-43f5-a9b9-d016087a6fa6",
                isFavorite = false
            ),
            Vacancy(
                id = "5c08e828-549f-4eb3-bba1-5235677252fb",
                name = "Android Developer в Amazon",
                description = "Задачи, которые могут стать твоими:\n\nРазработка новой функциональности мобильного приложения под Android, его архитектуры и исправление существующих недостатков;\nНаписание качественного, чистого, читаемого кода, code-review;\nРазработка общих архитектурных решений;\nВзаимодействие с менеджерами, дизайнерами, бекендерами, тестировщиками;\nПроактивно участвовать в жизни продукта: обсуждении требований, планировании проектов, проектировании дизайна, прототипов, спецификаций;\nДелиться технической экспертизой: предлагать, обсуждать и интегрировать новые решения;\nДекомпозировать, оценивать сроки реализации задач и выдерживать их;\nПроектировать клиент-серверное взаимодействие;\nРазбираться в чужом коде и проводить его рефакторинг;\nДоносить свои мысли и отстаивать свою точку зрения перед остальными членами команды;\nНе просто накидывать идеи, а реализовывать и доводить их до конца в общем проекте;\n\nЧто нужно знать:\n\nAndroid SDK, Android Support Libraries\nПаттерны построения мобильного UI/UX, принципы Material Desig\nПаттерны проектирования, ООП, SOLID, понимание функционального реактивного кода, Clean; Architecture\nKotlin\nDagger, Kotlin Coroutines, Kotlin Flow, Compose, MVVM / MVI, Room\nGradle Multi Modules\nЗнание архитектуры OS Android и особенностей его версий 21+\nВладение техническим английским языком на уровне чтения и понимания\n\nБудет плюсом:\n\nОпыт RxJava\nОпыт написания Unit и UI тестов\nОпыт в Backend Driven UI подходе\nРабота с Gradle\nОпыт работы с CI&DI",
                salary = "от 8 000 до 18 000 HK$",
                city = "Ульяновск",
                address = "Ульяновск, Ленина, 17",
                experience = "Нет опыта",
                conditions = "Полная занятость, Полный день",
                contact = Contact(
                    name = "Попов Андрей Сергеевич",
                    email = "123@gmail.com",
                    phones = listOf("+7 (999) 678-90-12", "+7 (999) 432-10-98")
                ),
                employerName = "Amazon",
                employerLogo = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Amazon_logo.svg/1200px-Amazon_logo.svg.png",
                skills = listOf("Swift", "CSS", "Python", "Ruby", "Go"),
                url = "cek6h0n4ffe7ur.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com/vacancies/5c08e828-549f-4eb3-bba1-5235677252fb",
                isFavorite = false
            )
        )
    }

    private fun searchMockVacancies(expression: String, page: Int): Flow<Pair<VacanciesPage?, SearchResultStatus>> = flow {
        delay(1000)
        val currentPage: VacanciesPage? = VacanciesPage(
            totalVacancies = vacanciesSearchResultsList.size,
            pageNumber = page,
            vacancies = vacanciesSearchResultsList.subList(
                (page - 1) * VACANCIES_PER_PAGE,
                if (page * VACANCIES_PER_PAGE > vacanciesSearchResultsList.size) vacanciesSearchResultsList.size else page * VACANCIES_PER_PAGE
            )
        )
        emit(
            Pair(
                if (page > 2) null else currentPage,
                if (page > 2) SearchResultStatus.ServerError else SearchResultStatus.Success
            )
        )
    }

}
