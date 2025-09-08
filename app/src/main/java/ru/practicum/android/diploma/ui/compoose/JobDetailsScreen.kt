package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.PlaceholderImages
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyState
import ru.practicum.android.diploma.ui.theme.ArrowBackHeight
import ru.practicum.android.diploma.ui.theme.ArrowBackTouchTarget
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.CornerRadius
import ru.practicum.android.diploma.ui.theme.ItemListImageSpaceWidth
import ru.practicum.android.diploma.ui.theme.LightGray
import ru.practicum.android.diploma.ui.theme.LineHeightRegularDp
import ru.practicum.android.diploma.ui.theme.LogoSize
import ru.practicum.android.diploma.ui.theme.NeutralDark
import ru.practicum.android.diploma.ui.theme.PaddingBase
import ru.practicum.android.diploma.ui.theme.PaddingSmall
import ru.practicum.android.diploma.ui.theme.Red
import ru.practicum.android.diploma.ui.viewmodel.JobDetailsViewModel

private const val WEIGHT_1F = 1F
private const val ONE_LINE = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(
    navController: NavController,
    viewModel: JobDetailsViewModel
) {
    val vacancyState = viewModel.vacancyState.collectAsState().value
    val favoriteState = viewModel.favoriteState.collectAsState().value

    LifecycleStartEffect(Unit) {
        viewModel.requestVacancyDetail()
        onStopOrDispose { }
    }

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
                        onClick = { viewModel.onShareClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(R.string.share),
                            modifier = Modifier.size(ArrowBackHeight)
                        )
                    }
                    IconButton(
                        onClick = { viewModel.onFavoriteClicked() }
                    ) {
                        Icon(
                            imageVector = if (favoriteState) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Filled.FavoriteBorder
                            },
                            tint = if (favoriteState) {
                                Red
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            contentDescription = stringResource(R.string.favorite),
                            modifier = Modifier.size(ArrowBackHeight)
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        when (vacancyState) {

            is VacancyState.Loading -> ProgressbarBox()

            is VacancyState.ServerError -> Placeholder(
                PlaceholderImages.JobDetailsServerError,
                stringResource(R.string.server_error)
            )

            is VacancyState.EmptyResult -> Placeholder(
                PlaceholderImages.JobDeleted,
                stringResource(R.string.no_connection)
            )

            is VacancyState.VacancyDetail -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(horizontal = PaddingBase)
                        .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.Start,
                ) {
                    VacancyTop(vacancyState.vacancy)
                    VacancyCard(vacancyState.vacancy)
                    VacancyExperienceConditions(vacancyState.vacancy)
                    VacancyDescription(vacancyState.vacancy)
                    VacancySkills(vacancyState.vacancy)
                    VacancyContacts(
                        vacancyState.vacancy,
                        { email -> viewModel.sendEmail(email) },
                        { phone -> viewModel.makeCall(phone) }
                    )
                }
            }
        }
    }
}

@Composable
private fun VacancyTop(vacancy: Vacancy) {
    Column(Modifier.padding(vertical = PaddingBase)) {
        Text(
            text = vacancy.name,
            maxLines = ONE_LINE,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = vacancy.salary,
            maxLines = ONE_LINE,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun VacancyCard(vacancy: Vacancy) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = LightGray,
            contentColor = NeutralDark,
        ),
        modifier = Modifier.fillMaxWidth().padding(vertical = PaddingBase)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingBase)
        ) {
            Box(modifier = Modifier.width(ItemListImageSpaceWidth)) {
                AsyncImage(
                    model = vacancy.employerLogo,
                    placeholder = painterResource(R.drawable.image_placeholder),
                    error = painterResource(R.drawable.image_placeholder),
                    contentDescription = vacancy.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(LogoSize)
                        .clip(RoundedCornerShape(CornerRadius)),
                )
            }
            Column {
                Text(
                    text = vacancy.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = vacancy.city,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun VacancyExperienceConditions(vacancy: Vacancy) {
    Column(Modifier.padding(vertical = PaddingBase)) {
        Text(
            text = stringResource(R.string.job_experience),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = vacancy.experience,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.padding(vertical = PaddingSmall),
            text = vacancy.conditions,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun VacancyDescription(vacancy: Vacancy) {
    Column(modifier = Modifier.padding(vertical = PaddingBase)) {
        Text(
            text = stringResource(R.string.job_description),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(vertical = PaddingBase),
            text = vacancy.description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun VacancySkills(vacancy: Vacancy) {
    if (vacancy.skills.isNotEmpty()) {
        val lazyColumnHeight = vacancy.skills.count() * LineHeightRegularDp.value
        Column() {
            Text(
                text = stringResource(R.string.job_skills),
                style = MaterialTheme.typography.titleMedium
            )
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = PaddingBase)
                    .height(lazyColumnHeight.dp)
            ) {
                items(vacancy.skills) { skill ->
                    Text(
                        modifier = Modifier.padding(horizontal = PaddingSmall),
                        text = "• $skill",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun VacancyContacts(vacancy: Vacancy, onEmailClick: (String) -> Unit, onPhoneClick: (String) -> Unit) {
    if (vacancy.contact.name.isNotEmpty()) {
        Column(Modifier.padding(vertical = PaddingBase)) {
            Text(
                modifier = Modifier.padding(bottom = PaddingBase),
                text = stringResource(R.string.job_contacts),
                style = MaterialTheme.typography.titleMedium
            )
            Column(Modifier.padding(vertical = PaddingSmall)) {
                Text(
                    text = stringResource(R.string.job_contact_name),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = vacancy.contact.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            if (vacancy.contact.email.isNotEmpty()) {
                Column(Modifier.padding(vertical = PaddingSmall)) {
                    Text(
                        text = stringResource(R.string.job_contact_email),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        modifier = Modifier.clickable {
                            onEmailClick.invoke(vacancy.contact.email)
                        },
                        text = vacancy.contact.email,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Blue
                    )
                }
            }
            if (vacancy.contact.phones.isNotEmpty()) {
                val lazyColumnHeight = vacancy.contact.phones.count() *
                    ( 2 * LineHeightRegularDp.value + 2 * PaddingSmall.value) +
                    2 * PaddingSmall.value
                LazyColumn(
                    modifier = Modifier.height(lazyColumnHeight.dp)
                ) {
                    items(vacancy.contact.phones) { phone ->
                        Column(Modifier.padding(vertical = PaddingSmall)) {
                            Text(
                                text = stringResource(R.string.job_contact_phone),
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                modifier = Modifier.clickable {
                                    onPhoneClick.invoke(phone.formatted)
                                },
                                text = phone.formatted,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Blue
                            )
                            if (phone.comment != null) {
                                Text(
                                    text = phone.comment,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
