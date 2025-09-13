package ru.practicum.android.diploma.ui.compoose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.PlaceholderImages
import ru.practicum.android.diploma.domain.models.SearchState
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.CornerRadius
import ru.practicum.android.diploma.ui.theme.CornerRadiusMedium
import ru.practicum.android.diploma.ui.theme.CornerRadiusSmall
import ru.practicum.android.diploma.ui.theme.IconButtonSize
import ru.practicum.android.diploma.ui.theme.ItemListImageSpaceWidth
import ru.practicum.android.diploma.ui.theme.LightGray
import ru.practicum.android.diploma.ui.theme.LogoSize
import ru.practicum.android.diploma.ui.theme.NeutralDark
import ru.practicum.android.diploma.ui.theme.NeutralLight
import ru.practicum.android.diploma.ui.theme.PaddingBase
import ru.practicum.android.diploma.ui.theme.PaddingSmall
import ru.practicum.android.diploma.ui.theme.ProgressItemHeight
import ru.practicum.android.diploma.ui.theme.SearchFieldContainerHeight
import ru.practicum.android.diploma.ui.theme.SearchFieldHeight
import ru.practicum.android.diploma.ui.theme.SpacerThin
import ru.practicum.android.diploma.ui.theme.TopBarHeight
import ru.practicum.android.diploma.ui.theme.TopToastHeight
import ru.practicum.android.diploma.ui.theme.Transparent
import ru.practicum.android.diploma.ui.theme.ZeroSize
import ru.practicum.android.diploma.ui.viewmodel.MainViewModel
import ru.practicum.android.diploma.util.debounceCompose

private const val WEIGHT_1F = 1F
private const val ONE_LINE = 1

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = koinViewModel<MainViewModel>()
) {
    val context = LocalContext.current

    // Отслеживаем основной объект состояний экрана поиска вакансий
    val searchState = viewModel.searchState.collectAsState().value

    // Отслеживаем признак активных фильтров для поиска
    val filtersEnabled = viewModel.filtersEnabledState.collectAsState().value

    // Отслеживаем возврат команды с экрана настройки фильтров для повторного поиска
    val filtersSetResult = navController.currentBackStackEntry?.savedStateHandle
        ?.getLiveData<Boolean>("repeatSearch")?.observeAsState()

    filtersSetResult?.value?.let {
        navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("repeatSearch")
        viewModel.searchDirectly()
    }

    // При возвращении на экран проверяем, возможно включились или выключились фильтры
    LifecycleResumeEffect(Unit) {
        viewModel.updateIsFiltersEnabled()
        onPauseOrDispose { }
    }

    // Формируем главную поверхность для макета экрана
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Сверху выводим заголовок с кнопкой настройки фильтров
        SearchTopBar(filtersEnabled = filtersEnabled) {
            viewModel.hideKeyboard()
            navController.navigate(ROUTE_FILTERS)
        }
        // Далее выводим поле ввода для поисковой строки
        SearchField(viewModel)

        // Содержимое главной области определяется на основании полученного состояния
        @Suppress("USELESS_IS_CHECK")
        when (searchState) {
            // Если строка поиска пуста, то выводим плейсхолдер начального состояния
            SearchState.EmptyString -> Placeholder(PlaceholderImages.EmptySearchString)

            // Во время ввода текста, в течение задержки по debounce ничего не показываем
            SearchState.InputInProgress -> Unit

            // Во время загрузки результатов выводим прогрессбар
            SearchState.Loading -> ProgressbarBox()

            // Состояние ошибки поиска из-за проблем с интернетом
            SearchState.NoConnection -> Placeholder(
                PlaceholderImages.NoConnection,
                stringResource(R.string.no_connection)
            )

            // Состояние ошибки поиска из-за проблем на сервере
            SearchState.ServerError -> Placeholder(
                PlaceholderImages.SearchServerError,
                stringResource(R.string.server_error)
            )

            // Состояние пустого результата запроса
            SearchState.EmptyResult -> {
                TopToast(stringResource(R.string.no_such_vacancies_found))
                Placeholder(PlaceholderImages.EmptyJobList, stringResource(R.string.empty_search_result))
            }

            // Состояние вывода списка найденных вакансий
            is SearchState.VacanciesFound -> {
                TopToast(
                    pluralStringResource(
                        R.plurals.numberOfVacancies,
                        searchState.vacanciesQuantity,
                        searchState.vacanciesQuantity
                    )
                )
                VacanciesList(
                    foundVacanciesList = searchState.vacanciesList,
                    isShowTrailingPlaceholder = searchState.isShowTrailingPlaceholder,
                    onLoadNextPage = {
                        viewModel.loadNextPage()
                    },
                    onItemClick = { vacancy ->
                        navController.navigate("$ROUTE_JOB_DETAILS/${vacancy.id}")
                    }
                )
                LaunchedEffect(searchState) {
                    if (searchState.errorMessage != null) {
                        Toast.makeText(context, searchState.errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

@Composable
fun SearchTopBar(filtersEnabled: Boolean, onFiltersClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(TopBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(PaddingBase).weight(WEIGHT_1F)) {
            TitleText(stringResource(R.string.title_search))
        }
        Box(modifier = Modifier.fillMaxHeight().width(TopBarHeight), contentAlignment = Alignment.Center) {
            FilterButton(filtersEnabled = filtersEnabled, onClick = onFiltersClick)
        }
    }
}

@Composable
fun FilterButton(filtersEnabled: Boolean, onClick: () -> Unit) {
    val buttonColors = when (filtersEnabled) {
        true -> IconButtonDefaults.filledIconButtonColors(
            containerColor = Blue,
            contentColor = NeutralLight
        )
        false -> IconButtonDefaults.filledIconButtonColors(
            containerColor = Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    }
    FilledIconButton(
        modifier = Modifier.size(IconButtonSize),
        shape = RoundedCornerShape(CornerRadiusSmall),
        colors = buttonColors,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.FilterList,
            contentDescription = stringResource(R.string.title_filters)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchField(viewModel: MainViewModel) {
    val searchText = viewModel.searchTextState.collectAsStateWithLifecycle().value

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(SearchFieldContainerHeight)
            .padding(horizontal = PaddingBase, vertical = PaddingSmall)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val enabled = true
        val isError = false
        val singleLine = true
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(Unit) {
            viewModel.hideKeyboardState.collect {
                keyboardController?.hide()
            }
        }

        BasicTextField(
            value = searchText,
            onValueChange = { viewModel.searchDebounce(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(SearchFieldHeight),
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = singleLine,
            cursorBrush = SolidColor(Blue),
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    viewModel.searchDirectly()
                }
            ),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = searchText,
                    innerTextField = innerTextField,
                    visualTransformation = VisualTransformation.None,
                    singleLine = singleLine,
                    enabled = enabled,
                    isError = isError,
                    trailingIcon = {
                        when (searchText.isNotEmpty()) {
                            true -> Icon(
                                modifier = Modifier
                                    .clickable(onClick = { viewModel.clearSearch() }),
                                tint = NeutralDark,
                                imageVector = Icons.Outlined.Close,
                                contentDescription = stringResource(R.string.title_search)
                            )
                            false -> Icon(
                                tint = NeutralDark,
                                imageVector = Icons.Outlined.Search,
                                contentDescription = stringResource(R.string.title_search)
                            )
                        }
                    },
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(start = PaddingBase),
                    placeholder = { Text(
                        text = stringResource(R.string.enter_request),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                    ) },
                    container = {
                        TextFieldDefaults.Container(
                            shape = RoundedCornerShape(CornerRadiusMedium),
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            unfocusedIndicatorLineThickness = ZeroSize,
                            focusedIndicatorLineThickness = ZeroSize,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Transparent,
                                unfocusedIndicatorColor = Transparent,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                        )
                    }
                )
            }
        )
    }
}

@Composable
fun TopToast(text: String) {
    Box(
        modifier = Modifier
            .height(TopToastHeight)
            .background(Blue, RoundedCornerShape(CornerRadius))
            .padding(horizontal = CornerRadius),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            maxLines = ONE_LINE,
            color = NeutralLight,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    Spacer(modifier = Modifier.height(PaddingSmall))
}

@Composable
fun VacanciesList(
    foundVacanciesList: List<Vacancy>,
    isShowTrailingPlaceholder: Boolean,
    onLoadNextPage: () -> Unit,
    onItemClick: (vacancy: Vacancy) -> Unit
) {
    val listState = rememberLazyListState()
    val shouldLoadNext = remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemsCount = listState.layoutInfo.totalItemsCount

            lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadNext.value) {
        if (shouldLoadNext.value) {
            onLoadNextPage()
        }
    }

    LazyColumn(
        Modifier.fillMaxSize(),
        state = listState
    ) {
        items(foundVacanciesList) { vacancy ->
            VacancyListItem(vacancy) {
                onItemClick(vacancy)
            }
        }
        if (isShowTrailingPlaceholder) {
            item {
                ProgressbarListItem()
            }
        }
    }
}

@Composable
fun VacancyListItem(vacancy: Vacancy, onClickAction: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingBase, vertical = PaddingSmall)
            .clickable(onClick = debounceCompose(action = onClickAction))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.width(ItemListImageSpaceWidth)) {
                val headers = NetworkHeaders.Builder()
                    .set("User-Agent", "Mozilla/5.0")
                    .build()
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(vacancy.employerLogo)
                        .httpHeaders(headers)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.image_placeholder),
                    error = painterResource(R.drawable.image_placeholder),
                    contentDescription = vacancy.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(LogoSize)
                        .border(width = SpacerThin, color = LightGray, shape = RoundedCornerShape(CornerRadius))
                        .clip(RoundedCornerShape(CornerRadius)),
                )
            }
            Column {
                Text(
                    text = "${vacancy.name}, ${vacancy.city}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = vacancy.employerName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = vacancy.salary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ProgressbarListItem() {
    Box(modifier = Modifier.fillMaxWidth().height(ProgressItemHeight)) {
        ProgressbarBox()
    }
}
