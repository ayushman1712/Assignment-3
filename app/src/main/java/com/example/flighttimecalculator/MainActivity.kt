package com.example.flighttimecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.flighttimecalculator.data.Flight
import com.example.flighttimecalculator.data.FlightDatabase
import com.example.flighttimecalculator.data.calculateAverageTime
import com.example.flighttimecalculator.ui.theme.FlightTimeCalculatorTheme
import com.example.flighttimecalculator.worker.scheduleFlightFetchWorker
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            FlightDatabase::class.java,
            "flight-db"
        ).build()

        scheduleFlightFetchWorker(applicationContext)

        setContent {
            FlightTimeCalculatorTheme {
                var avgTime by remember { mutableStateOf(0L) }

                LaunchedEffect(Unit) {
                    lifecycleScope.launch {
                        val flights = db.flightDao().getFlightsForRoute("Delhi", "Mumbai", "AI101")
                        avgTime = calculateAverageTime(flights)
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = "Avg Time for AI101 Delhi-Mumbai: ${avgTime / 60000} mins",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}