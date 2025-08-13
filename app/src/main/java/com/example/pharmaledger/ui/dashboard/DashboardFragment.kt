package com.example.pharmaledger.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.pharmaledger.databinding.FragmentDashboardBinding
import com.example.pharmaledger.ui.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class DashboardFragment : Fragment() {
	private var _binding: FragmentDashboardBinding? = null
	private val binding get() = _binding!!
	private val viewModel: DashboardViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentDashboardBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.chipGroupRange.setOnCheckedStateChangeListener { _, ids ->
			when (ids.firstOrNull()) {
				binding.chipDaily.id -> viewModel.setRange(SummaryRange.DAILY)
				binding.chipWeekly.id -> viewModel.setRange(SummaryRange.WEEKLY)
				binding.chipMonthly.id -> viewModel.setRange(SummaryRange.MONTHLY)
				else -> viewModel.setRange(SummaryRange.ALL)
			}
		}

		viewLifecycleOwner.lifecycleScope.launchWhenStarted {
			viewModel.uiState.collectLatest { state ->
				binding.valTotalMedicines.text = state.totalMedicines.toString()
				binding.valTotalSales.text = String.format("%.2f", state.totalSales)
				binding.valTotalExpenses.text = String.format("%.2f", state.totalExpenses)
				binding.valProfitLoss.text = String.format("%.2f", state.profitLoss)
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}