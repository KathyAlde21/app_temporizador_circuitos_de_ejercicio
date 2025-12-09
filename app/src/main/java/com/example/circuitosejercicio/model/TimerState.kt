package com.example.circuitosejercicio.model

data class TimerState(
    val totalDurationMillis: Long = 60_000L,      // 1 minuto por defecto
    val remainingMillis: Long = 60_000L,          // lo que queda
    val isRunning: Boolean = false,               // ¿está corriendo?
    val isFinished: Boolean = false               // ¿ya terminó?
)
