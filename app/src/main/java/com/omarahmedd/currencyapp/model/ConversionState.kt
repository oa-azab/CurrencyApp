package com.omarahmedd.currencyapp.model

data class ConversionState(
    val source: Currency? = null,
    val target: Currency? = null,
    val sourceAmount: String = "1.0",
    val targetAmount: String = " ",
    val message: String = ""
) {
    fun displayTransactionResult(): String {
        return "$sourceAmount ${source?.symbol} is equal to $targetAmount ${target?.symbol}"
    }
}