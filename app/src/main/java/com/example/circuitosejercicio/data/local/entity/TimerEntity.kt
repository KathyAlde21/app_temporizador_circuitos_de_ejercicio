package com.example.circuitosejercicio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer_state")
data class TimerEntity(

    // Siempre vamos a manejar UN solo temporizador, así que usamos id fijo = 1
    @PrimaryKey
    val id: Int = 1,

    // Duración total del circuito en milisegundos (ej: 60000 = 1 minuto)
    val totalDurationMillis: Long = 60_000L,

    // Tiempo restante en milisegundos la última vez que se guardó el estado
    val remainingMillis: Long = 60_000L,

    // Estado del temporizador: "IDLE", "RUNNING", "PAUSED", "FINISHED"
    val state: String = "IDLE",

    // Indica si el temporizador estaba corriendo cuando la app pasó a segundo plano
    val wasRunningBeforeBackground: Boolean = false,

    // Momento (en millis desde epoch) de la última actualización del estado
    val lastUpdatedAtMillis: Long = 0L
)
