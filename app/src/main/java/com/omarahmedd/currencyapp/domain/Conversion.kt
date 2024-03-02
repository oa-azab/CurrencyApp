package com.omarahmedd.currencyapp.domain

import android.util.Log
import com.omarahmedd.currencyapp.data.ExchangeRateRepository
import com.omarahmedd.currencyapp.model.ConversionState
import com.omarahmedd.currencyapp.model.Currency
import javax.inject.Inject


class Conversion @Inject constructor(
    private val repository: ExchangeRateRepository
) {

    private val zeroExchangeRateMessage = "These currencies are not supported in free version\n" +
            "Make sure that EUR is one conversion currencies"

    fun changeSource(newSource: Currency, state: ConversionState): ConversionState {
        val rate = repository.getExchangeRate(newSource, state.target)
        val newTargetAmount = convert(state.sourceAmount, rate)
        return state.copy(
            source = newSource,
            targetAmount = newTargetAmount,
            message = getConversionMessage(rate)
        )
    }

    fun changeTarget(newTarget: Currency, state: ConversionState): ConversionState {
        val rate = repository.getExchangeRate(state.source, newTarget)
        val newTargetAmount = convert(state.sourceAmount, rate)
        return state.copy(
            target = newTarget,
            targetAmount = newTargetAmount,
            message = getConversionMessage(rate)
        )
    }

    fun changeSourceAmount(newSourceAmount: String, state: ConversionState): ConversionState {
        val rate = repository.getExchangeRate(state.source, state.target)
        val newTargetAmount = convert(newSourceAmount, rate)
        return state.copy(
            sourceAmount = newSourceAmount,
            targetAmount = newTargetAmount,
            message = getConversionMessage(rate)
        )
    }

    fun changeTargetAmount(newTargetAmount: String, state: ConversionState): ConversionState {
        val rate = repository.getExchangeRate(state.source, state.target)
        val newSourceAmount = convert(newTargetAmount, 1 / rate)
        return state.copy(
            sourceAmount = newSourceAmount,
            targetAmount = newTargetAmount,
            message = getConversionMessage(rate)
        )
    }

    fun swapCurrencies(state: ConversionState): ConversionState {
        val newSource = state.target
        val newTarget = state.source
        val rate = repository.getExchangeRate(newSource, newTarget)
        val newTargetAmount = convert(state.sourceAmount, rate)
        return state.copy(
            source = newSource,
            target = newTarget,
            targetAmount = newTargetAmount,
            message = getConversionMessage(rate)
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

    private fun getConversionMessage(rate: Double) =
        if (rate == 0.0) zeroExchangeRateMessage else ""
}