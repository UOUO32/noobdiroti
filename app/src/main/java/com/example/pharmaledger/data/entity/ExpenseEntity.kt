package com.example.pharmaledger.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpenseEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val description: String,
	val amount: Double,
	val dateEpochMillis: Long
)