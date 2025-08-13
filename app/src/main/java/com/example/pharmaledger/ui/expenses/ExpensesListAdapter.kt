package com.example.pharmaledger.ui.expenses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmaledger.R
import com.example.pharmaledger.data.entity.ExpenseEntity

class ExpensesListAdapter : ListAdapter<ExpenseEntity, ExpensesListAdapter.VH>(Diff()) {
	class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val desc: TextView = itemView.findViewById(R.id.txt_expense_desc)
		val amount: TextView = itemView.findViewById(R.id.txt_expense_amount)
		val date: TextView = itemView.findViewById(R.id.txt_expense_date)
	}

	class Diff : DiffUtil.ItemCallback<ExpenseEntity>() {
		override fun areItemsTheSame(oldItem: ExpenseEntity, newItem: ExpenseEntity): Boolean = oldItem.id == newItem.id
		override fun areContentsTheSame(oldItem: ExpenseEntity, newItem: ExpenseEntity): Boolean = oldItem == newItem
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
		val v = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
		return VH(v)
	}

	override fun onBindViewHolder(holder: VH, position: Int) {
		val e = getItem(position)
		holder.desc.text = e.description
		holder.amount.text = "%.2f".format(e.amount)
		holder.date.text = android.text.format.DateFormat.format("dd/MM/yyyy", e.dateEpochMillis)
	}
}