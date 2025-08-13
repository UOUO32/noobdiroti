package com.example.pharmaledger.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	foreignKeys = [
		ForeignKey(
			entity = MedicineEntity::class,
			parentColumns = ["id"],
			childColumns = ["medicineId"],
			onDelete = ForeignKey.CASCADE
		)
	],
	indices = [Index(value = ["medicineId"])]
)
data class SaleEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val medicineId: Long,
	val quantity: Int,
	val totalAmount: Double,
	val dateEpochMillis: Long
)