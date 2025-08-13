package com.example.pharmaledger.data.dao

import androidx.room.*
import com.example.pharmaledger.data.entity.MedicineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
	@Query("SELECT * FROM MedicineEntity ORDER BY name ASC")
	fun observeAll(): Flow<List<MedicineEntity>>

	@Query("SELECT * FROM MedicineEntity WHERE id = :id")
	suspend fun getById(id: Long): MedicineEntity?

	@Query("SELECT * FROM MedicineEntity WHERE name = :name LIMIT 1")
	suspend fun findByExactName(name: String): MedicineEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(medicine: MedicineEntity): Long

	@Update
	suspend fun update(medicine: MedicineEntity)

	@Delete
	suspend fun delete(medicine: MedicineEntity)

	@Query("SELECT SUM(quantity) FROM MedicineEntity")
	fun observeTotalQuantity(): Flow<Int?>

	@Query("SELECT * FROM MedicineEntity WHERE name LIKE :query OR batchNumber LIKE :query ORDER BY name ASC")
	fun search(query: String): Flow<List<MedicineEntity>>

	@Query("SELECT * FROM MedicineEntity WHERE quantity <= :threshold ORDER BY quantity ASC")
	fun observeLowStock(threshold: Int): Flow<List<MedicineEntity>>

	@Query("SELECT * FROM MedicineEntity WHERE expiryDateEpochMillis <= :beforeEpoch ORDER BY expiryDateEpochMillis ASC")
	fun observeExpiringBefore(beforeEpoch: Long): Flow<List<MedicineEntity>>

	@Query("UPDATE MedicineEntity SET quantity = quantity - :qty WHERE id = :medicineId")
	suspend fun decrementStock(medicineId: Long, qty: Int)
}