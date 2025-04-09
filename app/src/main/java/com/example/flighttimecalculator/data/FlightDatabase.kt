package com.example.flighttimecalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Flight::class], version = 1)
abstract class FlightDatabase : RoomDatabase() {
    abstract fun flightDao(): FlightDao
}
