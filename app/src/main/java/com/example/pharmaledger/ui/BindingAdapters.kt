package com.example.pharmaledger.ui

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.addTextChangedListener(onChanged: (Editable?) -> Unit) {
	this.addTextChangedListener(object : TextWatcher {
		override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
		override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { onChanged(this@addTextChangedListener.text) }
		override fun afterTextChanged(s: Editable?) {}
	})
}