package ru.practicum.android.diploma.util

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DEBOUNCE_TIME = 2000L

fun <T> debounce(
    delayMillis: Long = DEBOUNCE_TIME,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}

@Composable
fun debounceCompose(debounceTime: Long = DEBOUNCE_TIME, action: () -> Unit): () -> Unit {
    val debouncedAction = remember(action) {
        var lastClickTime: Long = 0
        {
            if (SystemClock.elapsedRealtime() - lastClickTime >= debounceTime) {
                lastClickTime = SystemClock.elapsedRealtime()
                action()
            }
        }
    }
    return debouncedAction
}
