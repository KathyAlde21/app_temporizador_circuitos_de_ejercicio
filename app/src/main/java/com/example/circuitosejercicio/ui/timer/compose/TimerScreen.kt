package com.example.circuitosejercicio.ui.timer.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.circuitosejercicio.model.TimerState
import com.example.circuitosejercicio.ui.timer.TimerViewModel

@Composable
fun TimerScreen(
    viewModel: TimerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    TimerContent(
        state = uiState,
        onStartStopClick = { viewModel.onStartStopClicked() }
    )
}

@Composable
fun TimerContent(
    state: TimerState,
    onStartStopClick: () -> Unit
) {
    val secondsRemaining = (state.remainingMillis / 1000L).coerceAtLeast(0L)

    val buttonText = when {
        state.isRunning -> "Detener"
        state.isFinished -> "Reiniciar"
        else -> "Iniciar"
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Equivalente a un TextView en Compose
            Text(
                text = "${secondsRemaining}s",
                style = MaterialTheme.typography.headlineLarge
            )

            Button(
                onClick = onStartStopClick,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text(text = buttonText)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerContentPreview() {
    TimerContent(
        state = TimerState(
            totalDurationMillis = 60_000L,
            remainingMillis = 45_000L,
            isRunning = true,
            isFinished = false
        ),
        onStartStopClick = {}
    )
}
