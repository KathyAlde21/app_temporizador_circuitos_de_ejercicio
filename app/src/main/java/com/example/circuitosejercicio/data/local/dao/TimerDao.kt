package com.example.circuitosejercicio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.circuitosejercicio.data.local.entity.TimerEntity

@Dao
interface TimerDao {

    // Obtenemos SIEMPRE el registro con id = 1 (nuestro único temporizador)
    @Query("SELECT * FROM timer_state WHERE id = 1 LIMIT 1")
    suspend fun getTimerState(): TimerEntity?

    // Guardamos el estado del temporizador.
    // Si ya existe, lo reemplazamos.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTimerState(timerEntity: TimerEntity)
}

