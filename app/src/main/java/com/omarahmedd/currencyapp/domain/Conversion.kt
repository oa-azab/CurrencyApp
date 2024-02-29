package com.omarahmedd.currencyapp.domain

import android.util.Log


fun main() {
    val USD = Currency("USD", "US Dollar")
    val EUR = Currency("EUR", "Euro")

    val initState = ConversionState()
    println(initState.displayTransactionResult())

    val t0 = Conversion.changeSource(USD, initState)
    println(t0.displayTransactionResult())

    val t1 = Conversion.changeTarget(EUR, t0)
    println(t1.displayTransactionResult())

    val t2 = Conversion.changeSourceAmount("15.", t1)
    println(t2.displayTransactionResult())

    val t3 = Conversion.changeTargetAmount("50.0", t2)
    println(t3.displayTransactionResult())

    val t4 = Conversion.swapCurrencies(t3)
    println(t4.displayTransactionResult())
}

data class Currency(
    val symbol: String,
    val name: String
) {

    override fun toString(): String {
        return "$symbol - $name"
    }

    companion object {
        fun getCurrencies(): List<Currency> {
            return listOf(
                Currency("USD", "US Dollar"),
                Currency("EUR", "Euro"),
                Currency("EGP", "Egyptian Pound"),
            )
        }
    }
}


object ExchangeRate {

    fun getRate(source: Currency?, target: Currency?): Double {
        return when ("${source?.symbol}>${target?.symbol}") {
            "USD>EUR" -> 0.922216
            "EUR>USD" -> 1.0843452
            "USD>EGP" -> 30.90377
            "EGP>USD" -> 0.032358511
            "EUR>EGP" -> 33.388701
            "EGP>EUR" -> 0.029950252
            else -> 0.0
        }
    }

}

data class ConversionState(
    val source: Currency? = null,
    val target: Currency? = null,
    val sourceAmount: String = "1.0",
    val targetAmount: String = " ",
) {
    fun displayTransactionResult(): String {
        return "$sourceAmount ${source?.symbol} is equal to $targetAmount ${target?.symbol}"
    }
}


object Conversion {

    fun changeSource(newSource: Currency, state: ConversionState): ConversionState {
        val rate = ExchangeRate.getRate(newSource, state.target)
        val newTargetAmount = convert(state.sourceAmount, rate)
        return state.copy(
            source = newSource,
            targetAmount = newTargetAmount
        )
    }

    fun changeTarget(newTarget: Currency, state: ConversionState): ConversionState {
        val rate = ExchangeRate.getRate(state.source, newTarget)
        val newTargetAmount = convert(state.sourceAmount, rate)
        return state.copy(
            target = newTarget,
            targetAmount = newTargetAmount
        )
    }

    fun changeSourceAmount(newSourceAmount: String, state: ConversionState): ConversionState {
        val rate = ExchangeRate.getRate(state.source, state.target)
        val newTargetAmount = convert(newSourceAmount, rate)
        return state.copy(
            sourceAmount = newSourceAmount,
            targetAmount = newTargetAmount
        )
    }

    fun changeTargetAmount(newTargetAmount: String, state: ConversionState): ConversionState {
        val rate = ExchangeRate.getRate(state.source, state.target)
        val newSourceAmount = convert(newTargetAmount, 1 / rate)
        return state.copy(
            sourceAmount = newSourceAmount,
            targetAmount = newTargetAmount
        )
    }

    fun swapCurrencies(state: ConversionState): ConversionState {
        val newSource = state.target
        val newTarget = state.source
        val rate = ExchangeRate.getRate(newSource, newTarget)
        val newTargetAmount = convert(state.sourceAmount, rate)
        return state.copy(
            source = newSource,
            target = newTarget,
            targetAmount = newTargetAmount
        )
    }

    private fun convert(numberAsString: String, exchangeRate: Double): String {
        return try {
            val target = numberAsString.toDouble() * exchangeRate
            String.format("%.2f", target)
        } catch (t: Throwable) {
            Log.w("Conversion", t)
            " "
        }
    }
}