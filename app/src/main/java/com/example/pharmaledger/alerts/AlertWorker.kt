package com.example.pharmaledger.alerts

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.pharmaledger.R
import com.example.pharmaledger.data.AppDatabase
import com.example.pharmaledger.util.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class AlertWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
	override suspend fun doWork(): Result {
		val db = AppDatabase.getInstance(applicationContext)
		val lowThreshold = Preferences.getDefaultLowStock(applicationContext)
		val now = System.currentTimeMillis()
		val in30Days = now + 30L * 24L * 60L * 60L * 1000L

		val lowStockList = db.medicineDao().observeLowStock(lowThreshold).firstOrNull().orEmpty()
		val expiringList = db.medicineDao().observeExpiringBefore(in30Days).firstOrNull().orEmpty()

		if (lowStockList.isNotEmpty()) {
			notify("${applicationContext.getString(R.string.notification_low_stock_title)}: ${lowStockList.size}")
		}
		if (expiringList.isNotEmpty()) {
			notify("${applicationContext.getString(R.string.notification_expiry_title)}: ${expiringList.size}")
		}
		return Result.success()
	}

	private fun notify(message: String) {
		val manager = NotificationManagerCompat.from(applicationContext)
		val notification = NotificationCompat.Builder(applicationContext, "pharma_alerts")
			.setSmallIcon(android.R.drawable.ic_dialog_info)
			.setContentTitle(applicationContext.getString(R.string.app_name))
			.setContentText(message)
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.build()
		manager.notify(message.hashCode(), notification)
	}
}