package com.example.pharmaledger.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pharmaledger.data.AppDatabase
import com.example.pharmaledger.data.repo.ExpensesRepository
import com.example.pharmaledger.data.repo.MedicineRepository
import com.example.pharmaledger.data.repo.SalesRepository
import com.example.pharmaledger.ui.dashboard.DashboardViewModel
import com.example.pharmaledger.ui.expenses.ExpensesViewModel
import com.example.pharmaledger.ui.medicines.MedicinesViewModel
import com.example.pharmaledger.ui.sales.SalesViewModel

class ViewModelFactory private constructor(private val context: Context) : ViewModelProvider.Factory {
	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		val db = AppDatabase.getInstance(context)
		val medicineRepo = MedicineRepository(db.medicineDao())
		val salesRepo = SalesRepository(db.saleDao())
		val expensesRepo = ExpensesRepository(db.expenseDao())
		return when {
			modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(medicineRepo, salesRepo, expensesRepo) as T
			modelClass.isAssignableFrom(MedicinesViewModel::class.java) -> MedicinesViewModel(medicineRepo) as T
			modelClass.isAssignableFrom(SalesViewModel::class.java) -> SalesViewModel(salesRepo, medicineRepo) as T
			modelClass.isAssignableFrom(ExpensesViewModel::class.java) -> ExpensesViewModel(expensesRepo) as T
			else -> throw IllegalArgumentException("Unknown ViewModel class")
		}
	}

	companion object {
		fun getInstance(context: Context): ViewModelFactory = ViewModelFactory(context.applicationContext)
	}
}