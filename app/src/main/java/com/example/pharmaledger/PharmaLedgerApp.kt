package com.example.pharmaledger

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.pharmaledger.alerts.AlertWorker
import java.util.concurrent.TimeUnit

class PharmaLedgerApp : Application() {
	override fun onCreate() {
		super.onCreate()
		createNotificationChannels()
		scheduleAlerts()
	}

	private fun createNotificationChannels() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				"pharma_alerts",
				getString(R.string.notifications_channel_name),
				NotificationManager.IMPORTANCE_DEFAULT
			)
			channel.description = getString(R.string.notifications_channel_desc)
			val manager = getSystemService(NotificationManager::class.java)
			manager?.createNotificationChannel(channel)
		}
	}

	private fun scheduleAlerts() {
		val request = PeriodicWorkRequestBuilder<AlertWorker>(12, TimeUnit.HOURS).build()
		WorkManager.getInstance(this).enqueueUniquePeriodicWork(
			"pharma_alerts_worker",
			ExistingPeriodicWorkPolicy.KEEP,
			request
		)
	}
}