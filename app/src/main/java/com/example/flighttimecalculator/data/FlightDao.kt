package com.example.flighttimecalculator.data

import androidx.room.*

@Dao
interface FlightDao {
    @Insert
    suspend fun insertFlight(flight: Flight)

    @Query("SELECT * FROM flights WHERE origin = :origin AND destination = :destination AND flightNumber = :flightNumber")
    suspend fun getFlightsForRoute(origin: String, destination: String, flightNumber: String): List<Flight>
}