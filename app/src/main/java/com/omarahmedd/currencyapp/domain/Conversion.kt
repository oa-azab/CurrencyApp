package com.omarahmedd.currencyapp.domain


fun main() {
    val USD = Currency("USD", "US Dollar")
    val EUR = Currency("EUR", "Euro")

    val initState = State()
    println(initState.displayTransactionResult())

    val t0 = Conversion.changeSource(USD, initState)
    println(t0.displayTransactionResult())

    val t1 = Conversion.changeTarget(EUR, t0)
    println(t1.displayTransactionResult())

    val t2 = Conversion.changeSourceAmount(15.0, t1)
    println(t2.displayTransactionResult())

    val t3 = Conversion.changeTargetAmount(50.0, t2)
    println(t3.displayTransactionResult())

    val t4 = Conversion.swapCurrencies(t3)
    println(t4.displayTransactionResult())
}

data class Currency(
    val symbol: String,
    val name: String
) {
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
            else -> 0.0
        }
    }

}

data class State(
    val source: Currency? = null,
    val target: Currency? = null,
    val sourceAmount: Double = 1.0,
    val targetAmount: Double = 0.0,
) {
    fun displayTransactionResult(): String {
        return "$sourceAmount ${source?.symbol} is equal to $targetAmount ${target?.symbol}"
    }
}


object Conversion {

    fun changeSource(newSource: Currency, state: State): State {
        val rate = ExchangeRate.getRate(newSource, state.target)
        val newTargetAmount = state.sourceAmount * rate
        return state.copy(
            source = newSource,
            targetAmount = newTargetAmount
        )
    }

    fun changeTarget(newTarget: Currency, state: State): State {
        val rate = ExchangeRate.getRate(state.source, newTarget)
        val newTargetAmount = state.sourceAmount * rate
        return state.copy(
            target = newTarget,
            targetAmount = newTargetAmount
        )
    }

    fun changeSourceAmount(newSourceAmount: Double, state: State): State {
        val rate = ExchangeRate.getRate(state.source, state.target)
        val newTargetAmount = newSourceAmount * rate
        return state.copy(
            sourceAmount = newSourceAmount,
            targetAmount = newTargetAmount
        )
    }

    fun changeTargetAmount(newTargetAmount: Double, state: State): State {
        val rate = ExchangeRate.getRate(state.source, state.target)
        val newSourceAmount = newTargetAmount / rate
        return state.copy(
            sourceAmount = newSourceAmount,
            targetAmount = newTargetAmount
        )
    }

    fun swapCurrencies(state: State): State {
        val newSource = state.target
        val newTarget = state.source
        val rate = ExchangeRate.getRate(newSource, newTarget)
        val newTargetAmount = state.sourceAmount * rate
        return state.copy(
            source = newSource,
            target = newTarget,
            targetAmount = newTargetAmount
        )
    }
}