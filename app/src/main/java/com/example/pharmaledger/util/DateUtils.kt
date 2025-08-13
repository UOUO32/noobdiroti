package com.example.pharmaledger.util

import java.util.Calendar

object DateUtils {
	fun startOfDay(epochMillis: Long): Long {
		val cal = Calendar.getInstance().apply { timeInMillis = epochMillis }
		cal.set(Calendar.HOUR_OF_DAY, 0)
		cal.set(Calendar.MINUTE, 0)
		cal.set(Calendar.SECOND, 0)
		cal.set(Calendar.MILLISECOND, 0)
		return cal.timeInMillis
	}

	fun endOfDay(epochMillis: Long): Long {
		val cal = Calendar.getInstance().apply { timeInMillis = epochMillis }
		cal.set(Calendar.HOUR_OF_DAY, 23)
		cal.set(Calendar.MINUTE, 59)
		cal.set(Calendar.SECOND, 59)
		cal.set(Calendar.MILLISECOND, 999)
		return cal.timeInMillis
	}

	fun startOfWeek(epochMillis: Long): Long {
		val cal = Calendar.getInstance().apply { timeInMillis = epochMillis }
		cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
		return startOfDay(cal.timeInMillis)
	}

	fun endOfWeek(epochMillis: Long): Long {
		val cal = Calendar.getInstance().apply { timeInMillis = epochMillis }
		cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek + 6)
		return endOfDay(cal.timeInMillis)
	}

	fun startOfMonth(epochMillis: Long): Long {
		val cal = Calendar.getInstance().apply { timeInMillis = epochMillis }
		cal.set(Calendar.DAY_OF_MONTH, 1)
		return startOfDay(cal.timeInMillis)
	}

	fun endOfMonth(epochMillis: Long): Long {
		val cal = Calendar.getInstance().apply { timeInMillis = epochMillis }
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
		return endOfDay(cal.timeInMillis)
	}
}