package com.example.pharmaledger.ui.medicines

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmaledger.R
import com.example.pharmaledger.data.entity.MedicineEntity
import com.example.pharmaledger.databinding.FragmentMedicinesBinding
import com.example.pharmaledger.ui.ViewModelFactory
import com.example.pharmaledger.ui.addTextChangedListener
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar

class MedicinesFragment : Fragment() {
	private var _binding: FragmentMedicinesBinding? = null
	private val binding get() = _binding!!
	private val viewModel: MedicinesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
	private lateinit var adapter: MedicinesListAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentMedicinesBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		adapter = MedicinesListAdapter()
		binding.recyclerMedicines.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerMedicines.adapter = adapter

		binding.searchInput.addTextChangedListener { e -> viewModel.setQuery(e?.toString().orEmpty()) }
		binding.fabAddMedicine.setOnClickListener { showAddMedicineDialog() }
		binding.btnFilter.setOnClickListener { showFilterDialog() }

		viewLifecycleOwner.lifecycleScope.launchWhenStarted {
			viewModel.medicines.collectLatest { adapter.submitList(it) }
		}
	}

	private fun showFilterDialog() {
		val view = layoutInflater.inflate(R.layout.dialog_filter_medicines, null)
		val chkExpiry = view.findViewById<android.widget.CheckBox>(R.id.chk_expiring_soon)
		val inputExpiry = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.input_filter_expiry)
		val chkLow = view.findViewById<android.widget.CheckBox>(R.id.chk_low_stock)
		val inputThreshold = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.input_filter_threshold)
		var expEpoch = System.currentTimeMillis() + 30L * 24L * 60L * 60L * 1000L
		inputExpiry.setOnClickListener {
			val cal = Calendar.getInstance()
			DatePickerDialog(requireContext(), { _, y, m, d ->
				val c = Calendar.getInstance(); c.set(y, m, d, 0, 0, 0)
				expEpoch = c.timeInMillis
				inputExpiry.setText("%02d/%02d/%04d".format(d, m + 1, y))
			}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
		}
		com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
			.setTitle(R.string.filter)
			.setView(view)
			.setPositiveButton(R.string.save) { d, _ ->
				viewModel.setExpiringSoon(chkExpiry.isChecked, expEpoch)
				viewModel.setLowStock(chkLow.isChecked, inputThreshold.text?.toString()?.toIntOrNull() ?: 10)
				d.dismiss()
			}
			.setNegativeButton(R.string.cancel, null)
			.show()
	}

	private fun showAddMedicineDialog() {
		val dialogView = layoutInflater.inflate(com.example.pharmaledger.R.layout.dialog_add_medicine, null)
		val name = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_name)
		val batch = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_batch)
		val expiry = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_expiry)
		val category = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_category)
		val quantity = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_quantity)
		val purchase = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_purchase)
		val selling = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_selling)

		var expiryEpoch = System.currentTimeMillis()
		expiry.setOnClickListener {
			val cal = Calendar.getInstance()
			DatePickerDialog(requireContext(), { _, y, m, d ->
				val c = Calendar.getInstance(); c.set(y, m, d, 0, 0, 0)
				expiryEpoch = c.timeInMillis
				expiry.setText("%02d/%02d/%04d".format(d, m + 1, y))
			}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
		}

		val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
			.setTitle(com.example.pharmaledger.R.string.add_medicine)
			.setView(dialogView)
			.setPositiveButton(com.example.pharmaledger.R.string.save) { d, _ ->
				val entity = MedicineEntity(
					name = name.text?.toString().orEmpty(),
					batchNumber = batch.text?.toString().orEmpty(),
					expiryDateEpochMillis = expiryEpoch,
					quantity = quantity.text?.toString()?.toIntOrNull() ?: 0,
					purchasePricePerUnit = purchase.text?.toString()?.toDoubleOrNull() ?: 0.0,
					sellingPricePerUnit = selling.text?.toString()?.toDoubleOrNull() ?: 0.0,
					category = category.text?.toString()
				)
				viewModel.saveMedicine(entity)
				d.dismiss()
			}
			.setNegativeButton(com.example.pharmaledger.R.string.cancel, null)
			.create()
		dialog.show()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}