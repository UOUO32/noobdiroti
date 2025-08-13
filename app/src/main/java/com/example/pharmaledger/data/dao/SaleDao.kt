package com.example.pharmaledger.data.dao

import androidx.room.*
import com.example.pharmaledger.data.entity.SaleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {
	@Query("SELECT * FROM SaleEntity ORDER BY dateEpochMillis DESC")
	fun observeAll(): Flow<List<SaleEntity>>

	@Insert
	suspend fun insert(sale: SaleEntity): Long

	@Delete
	suspend fun delete(sale: SaleEntity)

	@Query("SELECT COALESCE(SUM(totalAmount), 0) FROM SaleEntity WHERE dateEpochMillis BETWEEN :start AND :end")
	fun observeTotalSalesBetween(start: Long, end: Long): Flow<Double>

	@Query("SELECT COALESCE(SUM(totalAmount), 0) FROM SaleEntity")
	fun observeTotalSalesAll(): Flow<Double>

	@Query("SELECT COALESCE(SUM(s.quantity * m.purchasePricePerUnit), 0) FROM SaleEntity s JOIN MedicineEntity m ON s.medicineId = m.id")
	fun observeTotalPurchaseCostAll(): Flow<Double>

	@Query("SELECT COALESCE(SUM(s.quantity * m.purchasePricePerUnit), 0) FROM SaleEntity s JOIN MedicineEntity m ON s.medicineId = m.id WHERE s.dateEpochMillis BETWEEN :start AND :end")
	fun observeTotalPurchaseCostBetween(start: Long, end: Long): Flow<Double>
}