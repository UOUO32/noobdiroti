package com.example.pharmaledger.backup

import android.content.Context
import android.net.Uri
import com.example.pharmaledger.data.AppDatabase
import com.example.pharmaledger.data.entity.ExpenseEntity
import com.example.pharmaledger.data.entity.MedicineEntity
import com.example.pharmaledger.data.entity.SaleEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.firstOrNull
import java.io.File

object BackupManager {
	data class BackupPayload(
		val medicines: List<MedicineEntity>,
		val sales: List<SaleEntity>,
		val expenses: List<ExpenseEntity>
	)

	suspend fun exportToFile(context: Context, file: File) {
		val payload = createPayload(context)
		file.writeText(Gson().toJson(payload))
	}

	suspend fun importFromFile(context: Context, file: File) {
		val json = file.readText()
		restoreFromJson(context, json)
	}

	suspend fun exportToUri(context: Context, uri: Uri) {
		val payload = createPayload(context)
		context.contentResolver.openOutputStream(uri)?.use { out ->
			out.write(Gson().toJson(payload).toByteArray())
		}
	}

	suspend fun importFromUri(context: Context, uri: Uri) {
		val json = context.contentResolver.openInputStream(uri)?.use { it.readBytes().toString(Charsets.UTF_8) } ?: return
		restoreFromJson(context, json)
	}

	private suspend fun createPayload(context: Context): BackupPayload {
		val db = AppDatabase.getInstance(context)
		val meds = db.medicineDao().observeAll().firstOrNull().orEmpty()
		val sales = db.saleDao().observeAll().firstOrNull().orEmpty()
		val expenses = db.expenseDao().observeAll().firstOrNull().orEmpty()
		return BackupPayload(meds, sales, expenses)
	}

	private suspend fun restoreFromJson(context: Context, json: String) {
		val db = AppDatabase.getInstance(context)
		val payload = Gson().fromJson<BackupPayload>(json, object : TypeToken<BackupPayload>() {}.type)
		db.clearAllTables()
		payload.medicines.forEach { db.medicineDao().insert(it) }
		payload.sales.forEach { db.saleDao().insert(it) }
		payload.expenses.forEach { db.expenseDao().insert(it) }
	}
}