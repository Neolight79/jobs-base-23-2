package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.practicum.android.diploma.data.TeamMember
import ru.practicum.android.diploma.ui.theme.PaddingBase

@Composable
fun TeamScreen(
    onBackClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TestTitle("Экран\nКОМАНДА")
            TeamComponent(
                teamMember = TeamMember(
                    name = "Владислав",
                    surname = "Сергеев",
                    currentJob = "Андроид разработчик",
                    githubName = "Vladismann",
                    imageRes = null,
                )
            )
            Spacer(Modifier.width(PaddingBase))
            TeamComponent(
                teamMember = TeamMember(
                    name = "Сергей",
                    surname = "Кулешов",
                    currentJob = "Андроид разработчик",
                    githubName = "Neolight79",
                    imageRes = null,
                )
            )
            Spacer(Modifier.width(PaddingBase))
            TeamComponent(
                teamMember = TeamMember(
                    name = "Евгений",
                    surname = "Колосов",
                    currentJob = "Андроид разработчик",
                    githubName = "owenear",
                    imageRes = null,
                )
            )
            Spacer(Modifier.width(PaddingBase))
            TeamComponent(
                teamMember = TeamMember(
                    name = "Елена",
                    surname = "Пупышева",
                    currentJob = "Андроид разработчик",
                    githubName = "ElenaPupysheva",
                    imageRes = null,
                )
            )
            Spacer(Modifier.width(PaddingBase))
            TestButton("Назад") {
                onBackClick()
            }
        }
    }
}
