package ru.practicum.android.diploma.ui.compoose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.practicum.android.diploma.domain.models.PlaceholderImages
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.PaddingBase
import ru.practicum.android.diploma.ui.theme.PaddingSmall
import ru.practicum.android.diploma.ui.theme.PlaceholderImageHeight
import ru.practicum.android.diploma.ui.theme.PlaceholderImageWidth
import ru.practicum.android.diploma.ui.theme.ProgressBarSize

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

@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun Placeholder(image: PlaceholderImages, text: String? = null) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.size(width = PlaceholderImageWidth, height = PlaceholderImageHeight),
                painter = painterResource(id = image.imageResId),
                contentDescription = text
            )
            if (!text.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(PaddingBase))
                Text(
                    textAlign = TextAlign.Center,
                    text = text,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun ProgressbarBox() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.width(ProgressBarSize),
            color = Blue
        )
    }
}
