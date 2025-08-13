package com.example.pharmaledger.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmaledger.data.repo.ExpensesRepository
import com.example.pharmaledger.data.repo.MedicineRepository
import com.example.pharmaledger.data.repo.SalesRepository
import com.example.pharmaledger.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class SummaryRange { DAILY, WEEKLY, MONTHLY, ALL }

data class DashboardUiState(
	val totalMedicines: Int = 0,
	val totalSales: Double = 0.0,
	val totalExpenses: Double = 0.0,
	val totalPurchaseCost: Double = 0.0,
	val profitLoss: Double = 0.0,
	val range: SummaryRange = SummaryRange.ALL
)

class DashboardViewModel(
	private val medicineRepository: MedicineRepository,
	private val salesRepository: SalesRepository,
	private val expensesRepository: ExpensesRepository
) : ViewModel() {
	private val selectedRange = MutableStateFlow(SummaryRange.ALL)

	private fun rangeStartEnd(now: Long, range: SummaryRange): Pair<Long, Long>? {
		return when (range) {
			SummaryRange.DAILY -> DateUtils.startOfDay(now) to DateUtils.endOfDay(now)
			SummaryRange.WEEKLY -> DateUtils.startOfWeek(now) to DateUtils.endOfWeek(now)
			SummaryRange.MONTHLY -> DateUtils.startOfMonth(now) to DateUtils.endOfMonth(now)
			SummaryRange.ALL -> null
		}
	}

	val uiState: StateFlow<DashboardUiState> = combine(
		selectedRange,
		medicineRepository.observeTotalQuantity(),
		salesRepository.observeTotalSalesAll(),
		expensesRepository.observeTotalExpensesAll(),
		salesRepository.observeTotalPurchaseCostAll()
	) { range, totalQtyOpt, totalSalesAll, totalExpensesAll, purchaseCostAll ->
		val now = System.currentTimeMillis()
		val rangePair = rangeStartEnd(now, range)
		if (rangePair == null) {
			val totalQty = totalQtyOpt ?: 0
			val profit = totalSalesAll - purchaseCostAll - totalExpensesAll
			DashboardUiState(totalQty, totalSalesAll, totalExpensesAll, purchaseCostAll, profit, range)
		} else {
			val (start, end) = rangePair
			// For ranged values we will recombine separate flows
			val totalSales = salesRepository.observeTotalSalesBetween(start, end).stateIn(viewModelScope, SharingStarted.Eagerly, 0.0).value
			val totalExpenses = expensesRepository.observeTotalExpensesBetween(start, end).stateIn(viewModelScope, SharingStarted.Eagerly, 0.0).value
			val purchaseCost = salesRepository.observeTotalPurchaseCostBetween(start, end).stateIn(viewModelScope, SharingStarted.Eagerly, 0.0).value
			val totalQty = totalQtyOpt ?: 0
			val profit = totalSales - purchaseCost - totalExpenses
			DashboardUiState(totalQty, totalSales, totalExpenses, purchaseCost, profit, range)
		}
	}.stateIn(viewModelScope, SharingStarted.Eagerly, DashboardUiState())

	fun setRange(range: SummaryRange) { selectedRange.value = range }
}