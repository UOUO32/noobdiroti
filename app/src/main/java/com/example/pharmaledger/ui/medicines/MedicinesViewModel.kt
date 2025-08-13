package com.example.pharmaledger.ui.medicines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmaledger.data.entity.MedicineEntity
import com.example.pharmaledger.data.repo.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedicinesViewModel(private val repository: MedicineRepository) : ViewModel() {
	private val query = MutableStateFlow("")
	private val filterLowStock = MutableStateFlow(false)
	private val filterExpiringSoon = MutableStateFlow(false)
	private val threshold = MutableStateFlow(10)
	private val expiringBeforeEpoch = MutableStateFlow(System.currentTimeMillis() + 30L * 24L * 60L * 60L * 1000L)

	val medicines: StateFlow<List<MedicineEntity>> = combine(
		query, filterLowStock, filterExpiringSoon, threshold, expiringBeforeEpoch, repository.observeAll()
	) { q, low, exp, th, expBefore, all ->
		val base = if (q.isBlank()) all else all.filter { it.name.contains(q, true) || it.batchNumber.contains(q, true) }
		base.filter { m -> (!low || m.quantity <= th) && (!exp || m.expiryDateEpochMillis <= expBefore) }
	}.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

	fun setQuery(text: String) { query.value = text }
	fun setLowStock(enabled: Boolean, limit: Int) { filterLowStock.value = enabled; threshold.value = limit }
	fun setExpiringSoon(enabled: Boolean, beforeEpoch: Long) { filterExpiringSoon.value = enabled; expiringBeforeEpoch.value = beforeEpoch }

	fun saveMedicine(m: MedicineEntity) = viewModelScope.launch { repository.upsert(m) }
	fun deleteMedicine(m: MedicineEntity) = viewModelScope.launch { repository.delete(m) }
}