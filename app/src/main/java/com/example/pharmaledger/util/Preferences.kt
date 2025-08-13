package com.example.pharmaledger.util

import android.content.Context
import androidx.preference.PreferenceManager

object Preferences {
	fun getCurrency(context: Context): String =
		PreferenceManager.getDefaultSharedPreferences(context).getString("pref_currency", "$") ?: "$"

	fun getDefaultLowStock(context: Context): Int =
		PreferenceManager.getDefaultSharedPreferences(context).getString("pref_stock_alert", "10")?.toIntOrNull() ?: 10

	fun getBackupLocation(context: Context): String =
		PreferenceManager.getDefaultSharedPreferences(context).getString("pref_backup_location", "local") ?: "local"
}