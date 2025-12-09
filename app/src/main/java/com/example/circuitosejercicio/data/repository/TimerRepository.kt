package com.example.circuitosejercicio.data.repository

import com.example.circuitosejercicio.data.local.dao.TimerDao
import com.example.circuitosejercicio.data.local.entity.TimerEntity

class TimerRepository(
    private val timerDao: TimerDao
) {

    suspend fun getTimerState(): TimerEntity {
        // Si no hay registro, devolvemos uno por defecto
        return timerDao.getTimerState() ?: TimerEntity()
    }

    suspend fun saveTimerState(timerEntity: TimerEntity) {
        timerDao.saveTimerState(timerEntity)
    }
}
