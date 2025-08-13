package com.example.pharmaledger.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	indices = [Index(value = ["name"], unique = false), Index(value = ["batchNumber"], unique = false)]
)
data class MedicineEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val name: String,
	val batchNumber: String,
	val expiryDateEpochMillis: Long,
	val quantity: Int,
	val purchasePricePerUnit: Double,
	val sellingPricePerUnit: Double,
	val category: String? = null
)