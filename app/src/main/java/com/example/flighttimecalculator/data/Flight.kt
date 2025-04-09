package com.example.flighttimecalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights")
data class Flight(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val flightNumber: String,
    val origin: String,
    val destination: String,
    val scheduledDeparture: Long,
    val actualDeparture: Long,
    val scheduledArrival: Long,
    val actualArrival: Long
)