package com.example.pharmaledger.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmaledger.data.entity.ExpenseEntity
import com.example.pharmaledger.data.repo.ExpensesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpensesViewModel(private val repository: ExpensesRepository) : ViewModel() {
	val expenses: StateFlow<List<ExpenseEntity>> = repository.observeAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

	fun addExpense(description: String, amount: Double, dateEpochMillis: Long) = viewModelScope.launch {
		repository.insert(ExpenseEntity(description = description, amount = amount, dateEpochMillis = dateEpochMillis))
	}
}