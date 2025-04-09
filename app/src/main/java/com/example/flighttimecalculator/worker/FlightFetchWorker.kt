package com.example.flighttimecalculator.worker

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.flighttimecalculator.data.Flight
import com.example.flighttimecalculator.data.FlightDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

fun createFlightFetchWorker(appContext: Context, workerParams: WorkerParameters): CoroutineWorker {
    return object : CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
            val apiKey = "YOUR_AVIATIONSTACK_API_KEY"

            try {
                val url = URL("http://api.aviationstack.com/v1/flights?access_key=$apiKey&dep_iata=DEL&arr_iata=BOM")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                val data = json.getJSONArray("data")

                val db = Room.databaseBuilder(
                    appContext,
                    FlightDatabase::class.java,
                    "flight-db"
                ).build()

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
                for (i in 0 until data.length()) {
                    val flightJson = data.getJSONObject(i)
                    val flightNumber = flightJson.getJSONObject("flight").getString("iata")
                    val departure = flightJson.getJSONObject("departure")
                    val arrival = flightJson.getJSONObject("arrival")

                    val origin = departure.getString("iata")
                    val destination = arrival.getString("iata")

                    val scheduledDeparture = sdf.parse(departure.getString("scheduled"))?.time ?: continue
                    val actualDeparture = sdf.parse(departure.getString("actual"))?.time ?: continue
                    val scheduledArrival = sdf.parse(arrival.getString("scheduled"))?.time ?: continue
                    val actualArrival = sdf.parse(arrival.getString("actual"))?.time ?: continue

                    val flight = Flight(
                        flightNumber = flightNumber,
                        origin = origin,
                        destination = destination,
                        scheduledDeparture = scheduledDeparture,
                        actualDeparture = actualDeparture,
                        scheduledArrival = scheduledArrival,
                        actualArrival = actualArrival
                    )
                    db.flightDao().insertFlight(flight)
                }

                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure()
            }
        }
    }
}