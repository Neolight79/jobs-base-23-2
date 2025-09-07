package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.ArrowBackHeight
import ru.practicum.android.diploma.ui.theme.ArrowBackTouchTarget
import ru.practicum.android.diploma.ui.theme.PaddingBase
import ru.practicum.android.diploma.ui.viewmodel.JobDetailsViewModel

private const val WEIGHT_1F = 1F
private const val ONE_LINE = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(
    navController: NavController,
    viewModel: JobDetailsViewModel
) {
    // Отслеживаем основной объект состояний экрана поиска вакансий
    val vacancyIdState = viewModel.vacancyIdState.collectAsState().value

    viewModel.getVacancyDetail(vacancyIdState)

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = stringResource(R.string.screen_job_details),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = ONE_LINE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = PaddingBase)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .size(ArrowBackTouchTarget)
                            .padding(start = PaddingBase)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.arrow_back),
                            modifier = Modifier.size(ArrowBackHeight)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share ,
                            contentDescription = stringResource(R.string.arrow_back),
                            modifier = Modifier.size(ArrowBackHeight)
                        )
                    }
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder ,
                            contentDescription = stringResource(R.string.arrow_back),
                            modifier = Modifier.size(ArrowBackHeight)
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TestTitle("Текущий идентификатор:")
                TitleText(vacancyIdState)
            }

        }
    }


}

