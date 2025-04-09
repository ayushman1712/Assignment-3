package com.example.frenstalker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frenstalker.ui.theme.FrenStalkerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrenStalkerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FlightTrackerUI()
                }
            }
        }
    }
}

@Composable
fun FlightTrackerUI() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var flightNumber by remember { mutableStateOf(TextFieldValue("")) }
    var flightInfo by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = flightNumber,
            onValueChange = { flightNumber = it },
            label = { Text("Enter Flight IATA Code (e.g. AI101)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (flightNumber.text.trim().isEmpty()) {
                Toast.makeText(context, "Please enter a valid flight code", Toast.LENGTH_SHORT).show()
            } else {
                isLoading = true
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val apiKey = "3c0ac5eb5cc11f12112cce3f0316a30b" // Replace with your API key
                        val flightCode = flightNumber.text.trim()
                        val url = URL("http://api.aviationstack.com/v1/flights?access_key=$apiKey&flight_iata=$flightCode")
                        val connection = url.openConnection() as HttpURLConnection
                        connection.setRequestProperty("User-Agent", "Mozilla/5.0")

                        val response = connection.inputStream.bufferedReader().use { it.readText() }
                        val json = JSONObject(response)
                        val data = json.getJSONArray("data")

                        if (data.length() > 0) {
                            val flight = data.getJSONObject(0)
                            val live = flight.optJSONObject("live")
                            if (live != null) {
                                val lat = live.getDouble("latitude")
                                val lon = live.getDouble("longitude")
                                flightInfo = "Live Location:\nLatitude: $lat\nLongitude: $lon"
                            } else {
                                flightInfo = "Flight found but not currently in the air."
                            }
                        } else {
                            flightInfo = "No data found for $flightCode"
                        }
                    } catch (e: Exception) {
                        flightInfo = "Error: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            }
        }) {
            Text("Track Flight")
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = flightInfo)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FrenStalkerTheme {
        FlightTrackerUI()
    }
}
