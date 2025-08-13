package com.example.pharmaledger.data.repo

import com.example.pharmaledger.data.dao.SaleDao
import com.example.pharmaledger.data.entity.SaleEntity
import kotlinx.coroutines.flow.Flow

class SalesRepository(private val saleDao: SaleDao) {
	fun observeAll(): Flow<List<SaleEntity>> = saleDao.observeAll()
	fun observeTotalSalesAll(): Flow<Double> = saleDao.observeTotalSalesAll()
	fun observeTotalSalesBetween(start: Long, end: Long): Flow<Double> = saleDao.observeTotalSalesBetween(start, end)

	suspend fun insert(sale: SaleEntity): Long = saleDao.insert(sale)
	suspend fun delete(sale: SaleEntity) = saleDao.delete(sale)
}