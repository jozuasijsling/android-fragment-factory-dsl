package com.jsijsling.androidx.initializerfragmentfactory.data

import java.text.NumberFormat
import java.util.Locale

data class Transaction(
    val id: Int,
    val value: MonetaryValue,
    val beneficiaryIban: String,
    val beneficiaryName: String,
    val description: String,
) {
    fun getFormattedValue(): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
            currency = value.currency
            minimumFractionDigits = value.currency.defaultFractionDigits
            maximumFractionDigits = value.currency.defaultFractionDigits
        }
        return numberFormat.format(value.amount)
    }
}
