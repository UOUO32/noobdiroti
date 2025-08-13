package com.example.pharmaledger.data.repo

import com.example.pharmaledger.data.dao.MedicineDao
import com.example.pharmaledger.data.entity.MedicineEntity
import kotlinx.coroutines.flow.Flow

class MedicineRepository(private val medicineDao: MedicineDao) {
	fun observeAll(): Flow<List<MedicineEntity>> = medicineDao.observeAll()
	fun search(query: String): Flow<List<MedicineEntity>> = medicineDao.search("%$query%")
	fun observeLowStock(threshold: Int): Flow<List<MedicineEntity>> = medicineDao.observeLowStock(threshold)
	fun observeExpiringBefore(beforeEpoch: Long): Flow<List<MedicineEntity>> = medicineDao.observeExpiringBefore(beforeEpoch)
	fun observeTotalQuantity(): Flow<Int?> = medicineDao.observeTotalQuantity()

	suspend fun upsert(medicine: MedicineEntity): Long = medicineDao.insert(medicine)
	suspend fun update(medicine: MedicineEntity) = medicineDao.update(medicine)
	suspend fun delete(medicine: MedicineEntity) = medicineDao.delete(medicine)
	suspend fun decrementStock(medicineId: Long, qty: Int) = medicineDao.decrementStock(medicineId, qty)
}