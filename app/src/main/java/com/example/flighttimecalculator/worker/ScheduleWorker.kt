package com.example.flighttimecalculator.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class FlightFetchWorkerWrapper(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = createFlightFetchWorker(applicationContext, this.params).doWork()
}

fun scheduleFlightFetchWorker(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<FlightFetchWorkerWrapper>(1, TimeUnit.DAYS).build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "FlightFetchWorker",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}