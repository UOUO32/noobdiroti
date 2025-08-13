package com.example.pharmaledger.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmaledger.data.AppDatabase
import com.example.pharmaledger.databinding.FragmentSalesBinding
import com.example.pharmaledger.ui.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SalesFragment : Fragment() {
	private var _binding: FragmentSalesBinding? = null
	private val binding get() = _binding!!
	private val viewModel: SalesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
	private lateinit var adapter: SalesListAdapter
	private val nameCache = mutableMapOf<Long, String>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentSalesBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		adapter = SalesListAdapter { id -> nameCache[id] ?: "ID $id" }
		binding.recyclerSales.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerSales.adapter = adapter
		binding.fabAddSale.setOnClickListener { showAddSaleDialog() }

		// Preload name cache
		viewLifecycleOwner.lifecycleScope.launch {
			val meds = AppDatabase.getInstance(requireContext()).medicineDao().observeAll().collectLatest { list ->
				list.forEach { nameCache[it.id] = it.name }
			}
		}

		viewLifecycleOwner.lifecycleScope.launchWhenStarted {
			viewModel.sales.collectLatest { adapter.submitList(it) }
		}
	}

	private fun showAddSaleDialog() {
		val dialogView = layoutInflater.inflate(com.example.pharmaledger.R.layout.dialog_add_sale, null)
		val medName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_sale_medicine)
		val qty = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_sale_quantity)
		val amt = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_sale_amount)
		com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
			.setTitle(com.example.pharmaledger.R.string.add_sale)
			.setView(dialogView)
			.setPositiveButton(com.example.pharmaledger.R.string.save) { d, _ ->
				val name = medName.text?.toString().orEmpty()
				val quantity = qty.text?.toString()?.toIntOrNull() ?: 0
				val amount = amt.text?.toString()?.toDoubleOrNull() ?: 0.0
				viewLifecycleOwner.lifecycleScope.launchWhenStarted {
					val db = com.example.pharmaledger.data.AppDatabase.getInstance(requireContext())
					val med = db.medicineDao().findByExactName(name)
					if (med != null) {
						viewModel.recordSale(med.id, quantity, amount)
					}
				}
				d.dismiss()
			}
			.setNegativeButton(com.example.pharmaledger.R.string.cancel, null)
			.show()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}