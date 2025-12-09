package com.example.circuitosejercicio.ui.timer

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.circuitosejercicio.data.local.entity.TimerEntity
import com.example.circuitosejercicio.data.repository.TimerRepository
import com.example.circuitosejercicio.model.TimerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel(
    private val repository: TimerRepository
) : ViewModel() {

    companion object {
        private const val DEFAULT_DURATION = 60_000L // 1 minuto
    }

    private val _uiState = MutableStateFlow(
        TimerState(
            totalDurationMillis = DEFAULT_DURATION,
            remainingMillis = DEFAULT_DURATION
        )
    )
    val uiState: StateFlow<TimerState> = _uiState.asStateFlow()

    private var countDownTimer: CountDownTimer? = null

    // Para saber si debemos reanudar automáticamente al volver de onStop()
    private var wasRunningWhenStopped: Boolean = false

    init {
        // Restaurar estado desde Room al iniciar el ViewModel
        viewModelScope.launch(Dispatchers.IO) {
            val entity = repository.getTimerState()

            val restoredState = TimerState(
                totalDurationMillis = entity.totalDurationMillis,
                remainingMillis = entity.remainingMillis,
                isRunning = entity.state == "RUNNING",
                isFinished = entity.state == "FINISHED"
            )

            _uiState.value = restoredState
        }
    }

    fun onStartStopClicked() {
        val current = _uiState.value

        if (current.isRunning) {
            pauseTimer(manual = true)
        } else {
            // Si está terminado o en 0, reseteamos a la duración total
            val baseState = if (current.isFinished || current.remainingMillis <= 0L) {
                current.copy(
                    remainingMillis = current.totalDurationMillis,
                    isFinished = false
                )
            } else {
                current
            }
            _uiState.value = baseState
            startTimer()
        }
    }

    private fun startTimer() {
        val current = _uiState.value
        if (current.isRunning || current.remainingMillis <= 0L) return

        countDownTimer?.cancel()

        val duration = current.remainingMillis

        countDownTimer = object : CountDownTimer(duration, 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                _uiState.value = _uiState.value.copy(
                    remainingMillis = millisUntilFinished,
                    isRunning = true,
                    isFinished = false
                )
            }

            override fun onFinish() {
                _uiState.value = _uiState.value.copy(
                    remainingMillis = 0L,
                    isRunning = false,
                    isFinished = true
                )
                saveToDatabase(state = "FINISHED", wasRunningBeforeBackground = false)
            }
        }.start()

        _uiState.value = current.copy(
            isRunning = true,
            isFinished = false
        )

        saveToDatabase(state = "RUNNING", wasRunningBeforeBackground = true)
    }

    private fun pauseTimer(manual: Boolean) {
        countDownTimer?.cancel()
        val current = _uiState.value

        _uiState.value = current.copy(
            isRunning = false,
            // si fue pausa manual, no marcamos como finished
            isFinished = if (manual) false else current.isFinished
        )

        saveToDatabase(
            state = if (_uiState.value.isFinished) "FINISHED" else "PAUSED",
            wasRunningBeforeBackground = false
        )
    }

    private fun saveToDatabase(
        state: String,
        wasRunningBeforeBackground: Boolean
    ) {
        val current = _uiState.value

        val entity = TimerEntity(
            id = 1,
            totalDurationMillis = current.totalDurationMillis,
            remainingMillis = current.remainingMillis,
            state = state,
            wasRunningBeforeBackground = wasRunningBeforeBackground,
            lastUpdatedAtMillis = System.currentTimeMillis()
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.saveTimerState(entity)
        }
    }

    // Llamado desde la Activity en onStop()
    fun onStopFromLifecycle() {
        val current = _uiState.value
        wasRunningWhenStopped = current.isRunning

        if (current.isRunning) {
            pauseTimer(manual = false)
        } else {
            // Igual guardamos el estado actual
            saveToDatabase(
                state = when {
                    current.isFinished -> "FINISHED"
                    current.remainingMillis == current.totalDurationMillis -> "IDLE"
                    else -> "PAUSED"
                },
                wasRunningBeforeBackground = false
            )
        }
    }

    // Llamado desde la Activity en onResume()
    fun onResumeFromLifecycle() {
        val current = _uiState.value
        if (wasRunningWhenStopped &&
            !current.isRunning &&
            !current.isFinished &&
            current.remainingMillis > 0L
        ) {
            startTimer()
            wasRunningWhenStopped = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }
}
