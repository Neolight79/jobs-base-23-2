package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.TeamMember
import ru.practicum.android.diploma.ui.theme.PaddingBase
import ru.practicum.android.diploma.ui.theme.PaddingSmall


@Composable
fun TeamComponent(teamMember: TeamMember) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = PaddingSmall,
                horizontal = PaddingBase
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (teamMember.imageRes != null) {
            Image(
                painter = painterResource(id = teamMember.imageRes),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        } else {
            RoundInitials(getInitials(teamMember))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = PaddingSmall)
        ) {
            Row() {
                Text(
                    text = teamMember.surname,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = teamMember.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Spacer(Modifier.width(2.dp))
            InfoRow(
                label = stringResource(R.string.сurrent_job),
                value = teamMember.currentJob ?: "—"
            )
            Spacer(Modifier.width(2.dp))
            InfoRow(
                label = stringResource(R.string.github_name),
                value = teamMember.githubName ?: "—"
            )
            Spacer(Modifier.width(2.dp))
        }
    }

}

@Composable
fun RoundInitials(initials: String) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Circle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxSize()
        )
        Text(text = initials)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "$label:",
            modifier = Modifier
                .weight(1f),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start

        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

fun getInitials(teamMember: TeamMember): String {
    val nameInitial = teamMember.name.firstOrNull()?.uppercaseChar()
    val familyInitial = teamMember.surname.firstOrNull()?.uppercaseChar()
    return listOfNotNull(nameInitial, familyInitial).joinToString("")
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Preview_WithImage() {
    val teamMember = TeamMember(
        name = "Имя",
        surname = "Фамилия",
        currentJob = "Андроид разработчик",
        githubName = "Megatron",
        imageRes = null,
    )
    TeamComponent(teamMember = teamMember)
}
