package ru.practicum.android.diploma.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ru.practicum.android.diploma.ui.compoose.NavScreen
import ru.practicum.android.diploma.ui.theme.DiplomaAppTheme

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiplomaAppTheme {
                NavScreen()
            }
        }
    }
}
