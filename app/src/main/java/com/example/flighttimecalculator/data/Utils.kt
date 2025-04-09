package com.example.flighttimecalculator.data

fun calculateAverageTime(flights: List<Flight>): Long {
    if (flights.isEmpty()) return 0L
    val totalTime = flights.sumOf { it.actualArrival - it.actualDeparture }
    return totalTime / flights.size
}