package com.example.pharmaledger.ui.expenses

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmaledger.databinding.FragmentExpensesBinding
import com.example.pharmaledger.ui.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar

class ExpensesFragment : Fragment() {
	private var _binding: FragmentExpensesBinding? = null
	private val binding get() = _binding!!
	private val viewModel: ExpensesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
	private lateinit var adapter: ExpensesListAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentExpensesBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		adapter = ExpensesListAdapter()
		binding.recyclerExpenses.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerExpenses.adapter = adapter
		binding.fabAddExpense.setOnClickListener { showAddExpenseDialog() }

		viewLifecycleOwner.lifecycleScope.launchWhenStarted {
			viewModel.expenses.collectLatest { adapter.submitList(it) }
		}
	}

	private fun showAddExpenseDialog() {
		val dialogView = layoutInflater.inflate(com.example.pharmaledger.R.layout.dialog_add_expense, null)
		val desc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_expense_desc)
		val amount = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_expense_amount)
		val date = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.pharmaledger.R.id.input_expense_date)
		var dateMillis = System.currentTimeMillis()
		date.setOnClickListener {
			val cal = Calendar.getInstance()
			DatePickerDialog(requireContext(), { _, y, m, d ->
				val c = Calendar.getInstance(); c.set(y, m, d, 0, 0, 0)
				dateMillis = c.timeInMillis
				date.setText("%02d/%02d/%04d".format(d, m + 1, y))
			}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
		}
		com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
			.setTitle(com.example.pharmaledger.R.string.add_expense)
			.setView(dialogView)
			.setPositiveButton(com.example.pharmaledger.R.string.save) { d, _ ->
				viewModel.addExpense(desc.text?.toString().orEmpty(), amount.text?.toString()?.toDoubleOrNull() ?: 0.0, dateMillis)
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