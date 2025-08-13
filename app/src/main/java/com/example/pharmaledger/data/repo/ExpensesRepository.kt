package com.example.pharmaledger.data.repo

import com.example.pharmaledger.data.dao.ExpenseDao
import com.example.pharmaledger.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpensesRepository(private val expenseDao: ExpenseDao) {
	fun observeAll(): Flow<List<ExpenseEntity>> = expenseDao.observeAll()
	fun observeTotalExpensesAll(): Flow<Double> = expenseDao.observeTotalExpensesAll()
	fun observeTotalExpensesBetween(start: Long, end: Long): Flow<Double> = expenseDao.observeTotalExpensesBetween(start, end)

	suspend fun insert(expense: ExpenseEntity): Long = expenseDao.insert(expense)
	suspend fun delete(expense: ExpenseEntity) = expenseDao.delete(expense)
}