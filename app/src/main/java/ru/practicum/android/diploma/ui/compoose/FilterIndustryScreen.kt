package ru.practicum.android.diploma.ui.compoose

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.IndustriesState
import ru.practicum.android.diploma.domain.models.PlaceholderImages
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.CornerRadiusMedium
import ru.practicum.android.diploma.ui.theme.NeutralDark
import ru.practicum.android.diploma.ui.theme.PaddingBase
import ru.practicum.android.diploma.ui.theme.PaddingSmall
import ru.practicum.android.diploma.ui.theme.SearchFieldContainerHeight
import ru.practicum.android.diploma.ui.theme.SearchFieldHeight
import ru.practicum.android.diploma.ui.theme.SettingsRowHeight
import ru.practicum.android.diploma.ui.theme.Transparent
import ru.practicum.android.diploma.ui.theme.ZeroSize
import ru.practicum.android.diploma.ui.viewmodel.FilterIndustryViewModel

private const val WEIGHT_1F = 1F

@Composable
fun FilterIndustryScreen(
    navController: NavController,
    viewModel: FilterIndustryViewModel = koinViewModel<FilterIndustryViewModel>()
) {
    // Отслеживаем основной объект состояний экрана выбора отрасли
    val industryState = viewModel.industryState.collectAsState().value

    // Флаг показа кнопки применения результатов
    var isIndustrySelected by remember { mutableStateOf(false) }

    // При попадании на экран запускаем загрузку отраслей
    LifecycleResumeEffect(Unit) {
        viewModel.search()
        onPauseOrDispose { }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Сверху выводим заголовок с кнопкой назад
        FiltersTopBar(stringResource(R.string.industry_selection)) {
            viewModel.hideKeyboard()
            navController.navigateUp()
        }
        Spacer(modifier = Modifier.height(PaddingBase))
        // Выводим поле для фильтрации региона
        IndustryFilterField(viewModel)
        // Дальнейшее содержимое зависит от текущего состояния
        when (industryState) {
            is IndustriesState.Loading -> ProgressbarBox()
            is IndustriesState.EmptyResult -> Placeholder(
                PlaceholderImages.EmptyJobList,
                stringResource(R.string.industry_not_found)
            )
            is IndustriesState.ServerError -> Placeholder(
                PlaceholderImages.JobDetailsServerError,
                stringResource(R.string.region_list_error)
            )
            is IndustriesState.Industries -> {
                isIndustrySelected = industryState.selectedItem != null || isIndustrySelected
                IndustriesList(
                    modifier = Modifier.fillMaxSize().weight(WEIGHT_1F),
                    selectedIndex = industryState.selectedItem?.id,
                    foundIndustriesList = industryState.industries,
                    onItemSelect = { industry ->
                        isIndustrySelected = true
                        viewModel.hideKeyboard()
                        viewModel.updateSelected(industry)
                    }
                )
            }
        }
        // Теперь место для кнопки применения выбора, в случае, если есть результат
        if (isIndustrySelected) {
            Spacer(modifier = Modifier.height(PaddingBase))
            ConfirmButton(stringResource(R.string.approve)) {
                viewModel.saveState()
                navController.navigateUp()
            }
        }
        Spacer(modifier = Modifier.height(PaddingBase))
        Spacer(modifier = Modifier.height(PaddingSmall))
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun IndustryFilterField(viewModel: FilterIndustryViewModel) {
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
            onValueChange = { viewModel.onSearchTextChange(it) },
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
                                contentDescription = stringResource(R.string.region_selection)
                            )
                            false -> Icon(
                                tint = NeutralDark,
                                imageVector = Icons.Outlined.Search,
                                contentDescription = stringResource(R.string.region_selection)
                            )
                        }
                    },
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(start = PaddingBase),
                    placeholder = { Text(
                        text = stringResource(R.string.enter_region),
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
fun IndustriesList(
    modifier: Modifier,
    selectedIndex: Int?,
    foundIndustriesList: List<FilterIndustry>,
    onItemSelect: (industry: FilterIndustry) -> Unit
) {
    val newSelectedIndex = remember { mutableStateOf(selectedIndex) }
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(foundIndustriesList) { industry ->
            RadioButtonItem(
                id = industry.id,
                title = industry.name.orEmpty(),
                selected = newSelectedIndex.value == industry.id,
                onSelect = {
                    newSelectedIndex.value = industry.id
                    onItemSelect(industry)
                }
            )
        }
    }
}

@Composable
fun RadioButtonItem(
    id: Int,
    title: String,
    selected: Boolean,
    onSelect: (id: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SettingsRowHeight)
            .selectable(
                selected = selected,
                onClick = {
                    onSelect(id)
                },
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = PaddingBase)
                .weight(WEIGHT_1F)
        ) {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(SettingsRowHeight),
            contentAlignment = Alignment.Center
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    unselectedColor = MaterialTheme.colorScheme.primary,
                    selectedColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
