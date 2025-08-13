package com.example.pharmaledger.ui.medicines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmaledger.R
import com.example.pharmaledger.data.entity.MedicineEntity

class MedicinesListAdapter : ListAdapter<MedicineEntity, MedicinesListAdapter.VH>(Diff()) {
	var onItemClick: ((MedicineEntity) -> Unit)? = null
	var onItemLongClick: ((MedicineEntity) -> Unit)? = null

	class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val name: TextView = itemView.findViewById(R.id.txt_name)
		val batchExpiry: TextView = itemView.findViewById(R.id.txt_batch_expiry)
		val qtyPrices: TextView = itemView.findViewById(R.id.txt_qty_prices)
	}

	class Diff : DiffUtil.ItemCallback<MedicineEntity>() {
		override fun areItemsTheSame(oldItem: MedicineEntity, newItem: MedicineEntity): Boolean = oldItem.id == newItem.id
		override fun areContentsTheSame(oldItem: MedicineEntity, newItem: MedicineEntity): Boolean = oldItem == newItem
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
		val v = LayoutInflater.from(parent.context).inflate(R.layout.item_medicine, parent, false)
		return VH(v)
	}

	override fun onBindViewHolder(holder: VH, position: Int) {
		val item = getItem(position)
		holder.name.text = item.name
		holder.batchExpiry.text = "${item.batchNumber} | ${android.text.format.DateFormat.format("dd/MM/yyyy", item.expiryDateEpochMillis)}"
		holder.qtyPrices.text = "Qty: ${item.quantity} | Buy: ${"%.2f".format(item.purchasePricePerUnit)} | Sell: ${"%.2f".format(item.sellingPricePerUnit)}"
		holder.itemView.setOnClickListener { onItemClick?.invoke(item) }
		holder.itemView.setOnLongClickListener { onItemLongClick?.invoke(item); true }
	}
}