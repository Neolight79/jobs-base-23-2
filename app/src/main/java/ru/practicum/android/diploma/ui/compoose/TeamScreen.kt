package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.TeamMember
import ru.practicum.android.diploma.ui.theme.ArrowBackHeight
import ru.practicum.android.diploma.ui.theme.ArrowBackTouchTarget
import ru.practicum.android.diploma.ui.theme.PaddingBase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = stringResource(R.string.screen_team),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = PaddingBase)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick() },
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
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TeamComponent(
                    teamMember = TeamMember(
                        name = "Владислав",
                        surname = "Сергеев",
                        currentJob = stringResource(R.string.android_developer),
                        githubName = "Vladismann",
                        imageRes = null,
                    )
                )
                TeamComponent(
                    teamMember = TeamMember(
                        name = "Сергей",
                        surname = "Кулешов",
                        currentJob = stringResource(R.string.android_developer),
                        githubName = "Neolight79",
                        imageRes = null,
                    )
                )
                TeamComponent(
                    teamMember = TeamMember(
                        name = "Евгений",
                        surname = "Колосов",
                        currentJob = stringResource(R.string.android_developer),
                        githubName = "owenear",
                        imageRes = null,
                    )
                )
                TeamComponent(
                    teamMember = TeamMember(
                        name = "Елена",
                        surname = "Пупышева",
                        currentJob = stringResource(R.string.android_developer),
                        githubName = "ElenaPupysheva",
                        imageRes = null,
                    )
                )
            }
            }
        }
}
