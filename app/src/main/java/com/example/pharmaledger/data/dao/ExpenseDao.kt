package com.example.pharmaledger.data.dao

import androidx.room.*
import com.example.pharmaledger.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
	@Query("SELECT * FROM ExpenseEntity ORDER BY dateEpochMillis DESC")
	fun observeAll(): Flow<List<ExpenseEntity>>

	@Insert
	suspend fun insert(expense: ExpenseEntity): Long

	@Delete
	suspend fun delete(expense: ExpenseEntity)

	@Query("SELECT COALESCE(SUM(amount), 0) FROM ExpenseEntity WHERE dateEpochMillis BETWEEN :start AND :end")
	fun observeTotalExpensesBetween(start: Long, end: Long): Flow<Double>

	@Query("SELECT COALESCE(SUM(amount), 0) FROM ExpenseEntity")
	fun observeTotalExpensesAll(): Flow<Double>
}