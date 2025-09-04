package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ru.practicum.android.diploma.ui.theme.PaddingSmall

@Composable
fun TestButton(text: String, onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(PaddingSmall))
    Button(
        onClick = onClick,
        content = {
            Text(
                text = text,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}

@Composable
fun TestTitle(
    text: String
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge
    )
}
