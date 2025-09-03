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
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.data.TeamMember

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
                    name = "Имя",
                    surname = "Фамилия",
                    currentJob = "Андроид разработчик",
                    githubName = "Megatron",
                    imageRes = null,
                )
            )
            Spacer(Modifier.width(2.dp))
            TestButton("Назад") {
                onBackClick()
            }
        }
    }
}
