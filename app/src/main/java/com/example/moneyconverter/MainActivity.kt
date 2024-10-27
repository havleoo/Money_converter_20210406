package com.example.moneyconverter

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var sourceAmount: EditText
    private lateinit var targetAmount: EditText
    private lateinit var sourceCurrency: Spinner
    private lateinit var targetCurrency: Spinner
    private var isSourceSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sourceAmount = findViewById(R.id.sourceAmount)
        targetAmount = findViewById(R.id.targetAmount)
        sourceCurrency = findViewById(R.id.sourceCurrency)
        targetCurrency = findViewById(R.id.targetCurrency)

        val currencies = arrayOf("VND", "USD", "EUR", "KRW", "JPY")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sourceCurrency.adapter = adapter
        targetCurrency.adapter = adapter

        // Bắt sự kiện khi chọn EditText nào
        sourceAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                isSourceSelected = true
            }
        }

        targetAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                isSourceSelected = false
            }
        }

        // Thêm TextWatcher cho sourceAmount
        sourceAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isSourceSelected) {
                    convertCurrency()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Thêm TextWatcher cho targetAmount
        targetAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isSourceSelected) {
                    convertCurrency()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun convertCurrency() {
        val sourceValue = if (isSourceSelected) sourceAmount.text.toString().toDoubleOrNull() else targetAmount.text.toString().toDoubleOrNull()
        if (sourceValue != null) {
            val rate = getConversionRate(
                if (isSourceSelected) sourceCurrency.selectedItem.toString() else targetCurrency.selectedItem.toString(),
                if (isSourceSelected) targetCurrency.selectedItem.toString() else sourceCurrency.selectedItem.toString()
            )
            val result = sourceValue * rate

            if (isSourceSelected) {
                targetAmount.setText(result.toString())
            } else {
                sourceAmount.setText(result.toString())
            }
        }
    }

    private fun getConversionRate(fromCurrency: String, toCurrency: String): Double {
        return when (fromCurrency to toCurrency) {
            "USD" to "VND" -> 23000.0
            "VND" to "USD" -> 1 / 23000.0
            "USD" to "EUR" -> 0.85
            "EUR" to "USD" -> 1 / 0.85
            "USD" to "KRW" -> 1340.0
            "KRW" to "USD" -> 1 / 1340.0
            "USD" to "JPY" -> 110.0
            "JPY" to "USD" -> 1 / 110.0
            "VND" to "EUR" -> 1 / 27000.0
            "EUR" to "VND" -> 27000.0
            "VND" to "KRW" -> 1 / 20.0
            "KRW" to "VND" -> 20.0
            "VND" to "JPY" -> 1 / 210.0
            "JPY" to "VND" -> 210.0
            "EUR" to "KRW" -> 1 / 0.00063
            "KRW" to "EUR" -> 0.00063
            "EUR" to "JPY" -> 130.0
            "JPY" to "EUR" -> 1 / 130.0
            "KRW" to "JPY" -> 0.091
            "JPY" to "KRW" -> 1 / 0.091
            else -> 1.0
        }
    }
}