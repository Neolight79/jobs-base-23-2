package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.ArrowBackHeight
import ru.practicum.android.diploma.ui.theme.ArrowBackTouchTarget
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.CornerRadius
import ru.practicum.android.diploma.ui.theme.Gray
import ru.practicum.android.diploma.ui.theme.NeutralDark
import ru.practicum.android.diploma.ui.theme.PaddingBase
import ru.practicum.android.diploma.ui.theme.PaddingSmall
import ru.practicum.android.diploma.ui.theme.Red
import ru.practicum.android.diploma.ui.theme.SalaryFieldHeight
import ru.practicum.android.diploma.ui.theme.SalaryRowHeight
import ru.practicum.android.diploma.ui.theme.SettingsRowHeight
import ru.practicum.android.diploma.ui.theme.SpacerThinTwo
import ru.practicum.android.diploma.ui.theme.TopBarHeight
import ru.practicum.android.diploma.ui.theme.Transparent
import ru.practicum.android.diploma.ui.theme.ZeroSize
import ru.practicum.android.diploma.ui.viewmodel.FiltersViewModel

private const val WEIGHT_1F = 1F
private const val ONE_LINE = 1

@Composable
fun FiltersScreen(
    navController: NavController,
    viewModel: FiltersViewModel = koinViewModel<FiltersViewModel>()
) {
    // Отслеживаем основной объект состояний экрана настройки фильтров
    val filtersState = viewModel.filtersState.collectAsState().value

    // При возвращении на экран загружаем заново настройки фильтров
    LifecycleResumeEffect(Unit) {
        viewModel.loadFilters()
        onPauseOrDispose { }
    }

    // Формируем главную поверхность для макета экрана
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Сверху выводим заголовок с кнопкой назад
        FiltersTopBar {
            viewModel.hideKeyboard()
            navController.navigateUp()
        }
        Spacer(modifier = Modifier.height(PaddingBase))
        // Далее выводим две строки для выбора места работы и отрасли
        SettingsRow(
            hint = stringResource(R.string.job_location),
            data = filtersState.location,
            onClick = {
                navController.navigate(ROUTE_FILTER_LOCATION)
            },
            onClear = {
                viewModel.clearLocation()
            }
        )
        SettingsRow(
            hint = stringResource(R.string.industry),
            data = filtersState.industry,
            onClick = {
                navController.navigate(ROUTE_FILTER_INDUSTRY)
            },
            onClear = {
                viewModel.clearIndustry()
            }
        )
        Spacer(modifier = Modifier.height(PaddingBase))
        // Далее выводим поле ввода для размера целевой зарплаты
        SalaryField(viewModel)
        Spacer(modifier = Modifier.height(PaddingBase))
        // Далее выводим поле галочки фильтрации вакансий без зарплаты
        CheckboxRow(
            stringResource(R.string.do_not_show_wo_sallary),
            filtersState.doNotShowWithoutSalary
        ) { isSelected ->
            viewModel.toggleDoNotShowWithoutSalary(isSelected)
        }
        Spacer(modifier = Modifier.weight(WEIGHT_1F))
        if (filtersState.areFiltersSet) {
            // Далее выводим кнопку применения настроек фильтрации и выполнения повторного поиска
            ConfirmButton(stringResource(R.string.confirm)) {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("repeatSearch", true)
                navController.popBackStack()
            }
            Spacer(modifier = Modifier.height(PaddingSmall))
            // Далее выводим кнопку сброса настроек фильтрации
            CancelButton(stringResource(R.string.reset)) {
                viewModel.resetFilters()
            }
        }
        Spacer(modifier = Modifier.height(PaddingBase))
        Spacer(modifier = Modifier.height(PaddingSmall))
    }
}

@Composable
fun FiltersTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(TopBarHeight),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(ArrowBackTouchTarget)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.arrow_back),
                    modifier = Modifier.size(ArrowBackHeight)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(ZeroSize)
                .weight(WEIGHT_1F)
        ) {
            TitleText(stringResource(R.string.title_filters))
        }
    }
}

@Composable
fun ConfirmButton(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(SettingsRowHeight)
            .padding(horizontal = PaddingBase)
    ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(CornerRadius),
            onClick = onClick,
            content = {
                Text(
                    text = title,
                    maxLines = ONE_LINE,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        )
    }
}

@Composable
fun CancelButton(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(SettingsRowHeight)
            .padding(horizontal = PaddingBase)
    ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(CornerRadius),
            colors = ButtonDefaults.buttonColors(
                contentColor = Red,
                containerColor = Transparent
            ),
            onClick = onClick,
            content = {
                Text(
                    text = title,
                    maxLines = ONE_LINE,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        )
    }
}

@Composable
fun CheckboxRow(
    title: String,
    checked: Boolean,
    onToggle: (isChecked: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SettingsRowHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = PaddingBase)
                .weight(WEIGHT_1F)
        ) {
            Text(
                text = title,
                maxLines = ONE_LINE,
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
            Checkbox(
                checked = checked,
                colors = CheckboxDefaults.colors(
                    uncheckedColor = MaterialTheme.colorScheme.primary,
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.background
                ),
                onCheckedChange = {
                    onToggle(!checked)
                }
            )
        }
    }
}

@Composable
fun SettingsRow(
    hint: String,
    data: String,
    onClear: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SettingsRowHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val clickAction = if (!data.isEmpty()) onClear else onClick
        val icon =
            if (!data.isEmpty()) painterResource(R.drawable.close) else painterResource(R.drawable.arrow_forward)
        val desc = if (!data.isEmpty()) data else hint
        Box(
            modifier = Modifier
                .padding(start = PaddingBase)
                .weight(WEIGHT_1F)
        ) {
            SettingsTextField(
                hint = hint,
                data = data,
                onClick = onClick
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(SettingsRowHeight),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = clickAction,
                modifier = Modifier
                    .size(ArrowBackTouchTarget)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = desc,
                    modifier = Modifier.size(ArrowBackHeight)
                )
            }
        }
    }
}

@Composable
fun SettingsTextField(hint: String, data: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        val text = if (!data.isEmpty()) data else hint
        val textColor = if (!data.isEmpty()) MaterialTheme.colorScheme.onBackground else Gray
        if (!data.isEmpty()) {
            Text(
                text = hint,
                maxLines = ONE_LINE,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                color = textColor
            )
        }
        Text(
            text = text,
            maxLines = ONE_LINE,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun SalaryField(viewModel: FiltersViewModel) {
    val salaryText = viewModel.filtersState.collectAsStateWithLifecycle().value.salary
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val enabled = true
    val isError = false
    val singleLine = true
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.hideKeyboardState.collect {
            keyboardController?.hide()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .height(SalaryRowHeight)
            .padding(
                horizontal = PaddingBase,
                vertical = PaddingSmall
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(SalaryFieldHeight).background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(CornerRadius)
                )
                .padding(start = PaddingBase),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(WEIGHT_1F)
            ) {
                Spacer(modifier = Modifier.height(SpacerThinTwo))
                Text(
                    text = stringResource(R.string.expected_salary),
                    maxLines = ONE_LINE,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isFocused) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                BasicTextField(
                    value = salaryText,
                    onValueChange = {
                        val filteredValue = it.filter { it.isDigit() }
                        if (filteredValue.isEmpty()) {
                            viewModel.saveSalary(filteredValue)
                        } else if (filteredValue.toInt() > 0) {
                            viewModel.saveSalary(filteredValue)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            isFocused = it.isFocused
                        },
                    interactionSource = interactionSource,
                    enabled = enabled,
                    singleLine = singleLine,
                    cursorBrush = SolidColor(Blue),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Decimal),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    decorationBox = { innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            value = salaryText,
                            visualTransformation = VisualTransformation.None,
                            innerTextField = innerTextField,
                            singleLine = singleLine,
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            contentPadding = PaddingValues(ZeroSize),
                            placeholder = { Text(
                                text = stringResource(R.string.expected_salary),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            ) },
                            container = {
                                TextFieldDefaults.Container(
                                    enabled = enabled,
                                    isError = isError,
                                    interactionSource = interactionSource,
                                    unfocusedIndicatorLineThickness = ZeroSize,
                                    focusedIndicatorLineThickness = ZeroSize,
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Transparent,
                                        unfocusedIndicatorColor = Transparent,
                                        focusedContainerColor = Transparent,
                                        unfocusedContainerColor = Transparent
                                    ),
                                )
                            }
                        )
                    }
                )
            }
            if (salaryText.isNotEmpty()) {
                IconButton(
                    onClick = {
                        viewModel.saveSalary("")
                    },
                    modifier = Modifier
                        .size(ArrowBackTouchTarget)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.close),
                        tint = NeutralDark,
                        contentDescription = stringResource(R.string.expected_salary),
                        modifier = Modifier.size(ArrowBackHeight)
                    )
                }
            }
        }
    }
}
