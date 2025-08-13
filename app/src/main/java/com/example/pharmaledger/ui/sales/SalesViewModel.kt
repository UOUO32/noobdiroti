package com.example.pharmaledger.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmaledger.data.entity.SaleEntity
import com.example.pharmaledger.data.repo.MedicineRepository
import com.example.pharmaledger.data.repo.SalesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SalesViewModel(
	private val salesRepository: SalesRepository,
	private val medicineRepository: MedicineRepository
) : ViewModel() {
	val sales: StateFlow<List<SaleEntity>> = salesRepository.observeAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

	fun recordSale(medicineId: Long, quantity: Int, totalAmount: Double) = viewModelScope.launch {
		salesRepository.insert(SaleEntity(medicineId = medicineId, quantity = quantity, totalAmount = totalAmount, dateEpochMillis = System.currentTimeMillis()))
		medicineRepository.decrementStock(medicineId, quantity)
	}
}