package com.example.pharmaledger.ui.sales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmaledger.R
import com.example.pharmaledger.data.entity.SaleEntity

class SalesListAdapter(
	private val resolveMedicineName: (Long) -> String = { id -> "ID $id" }
) : ListAdapter<SaleEntity, SalesListAdapter.VH>(Diff()) {
	class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val med: TextView = itemView.findViewById(R.id.txt_sale_medicine)
		val qtyAmt: TextView = itemView.findViewById(R.id.txt_sale_qty_amount)
		val date: TextView = itemView.findViewById(R.id.txt_sale_date)
	}

	class Diff : DiffUtil.ItemCallback<SaleEntity>() {
		override fun areItemsTheSame(oldItem: SaleEntity, newItem: SaleEntity): Boolean = oldItem.id == newItem.id
		override fun areContentsTheSame(oldItem: SaleEntity, newItem: SaleEntity): Boolean = oldItem == newItem
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
		val v = LayoutInflater.from(parent.context).inflate(R.layout.item_sale, parent, false)
		return VH(v)
	}

	override fun onBindViewHolder(holder: VH, position: Int) {
		val s = getItem(position)
		holder.med.text = resolveMedicineName(s.medicineId)
		holder.qtyAmt.text = "Qty: ${s.quantity} | Amount: ${"%.2f".format(s.totalAmount)}"
		holder.date.text = android.text.format.DateFormat.format("dd/MM/yyyy", s.dateEpochMillis)
	}
}