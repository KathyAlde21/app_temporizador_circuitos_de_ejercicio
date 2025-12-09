package com.example.circuitosejercicio.ui.timer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.circuitosejercicio.data.local.AppDatabase
import com.example.circuitosejercicio.data.repository.TimerRepository
import com.example.circuitosejercicio.ui.timer.compose.TimerScreen

class TimerActivity : ComponentActivity() {

    private lateinit var viewModel: TimerViewModel

    companion object {
        private const val TAG = "TimerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")

        // Instanciar Room + Repository + ViewModel
        val db = AppDatabase.getInstance(applicationContext)
        val timerDao = db.timerDao()
        val repository = TimerRepository(timerDao)
        val factory = TimerViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[TimerViewModel::class.java]

        setContent {
            TimerScreen(viewModel = viewModel)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume()")
        viewModel.onResumeFromLifecycle()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop()")
        viewModel.onStopFromLifecycle()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }
}
