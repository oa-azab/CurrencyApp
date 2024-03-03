package com.omarahmedd.currencyapp.domain

import com.omarahmedd.currencyapp.data.CurrencyRepository
import com.omarahmedd.currencyapp.data.ExchangeRateRepository
import com.omarahmedd.currencyapp.model.Currency
import com.omarahmedd.currencyapp.model.HistoricalEntry
import com.omarahmedd.currencyapp.model.HistoricalState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetHistoricalDataUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val repository: ExchangeRateRepository
) {

    suspend fun invoke(
        currenciesStr: String,
        numberOfPreviousDays: Int = 3
    ): UCResult<HistoricalState> {
        return try {
            val (base, target) = parseCurrenciesString(currenciesStr)
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val (targetSymbol, useInverseRate) = getTargetSymbolAndUseInverse(base, target)

            val entries = mutableListOf<HistoricalEntry>()
            repeat(numberOfPreviousDays) {
                calendar.add(Calendar.DAY_OF_MONTH, -1) // Move one day back
                val date = dateFormat.format(calendar.time)
                val rate = repository.getExchangeRateAt(date, targetSymbol)
                val inverseRate = 1 / rate
                val sendRate = if (useInverseRate) inverseRate else rate
                entries += HistoricalEntry(date, sendRate)
            }

            UCResult.Success(HistoricalState(base, target, entries))
        } catch (t: Throwable) {
            UCResult.Error(t)
        }
    }


    private suspend fun parseCurrenciesString(str: String): Pair<Currency, Currency> {
        val currencyParts = str.split("-")
        if (currencyParts.size != 2)
            throw Exception("Currencies string invalid $str, it should be Base-Target")

        val allCurrencies = currencyRepository.getCurrencies()
        val base = allCurrencies.find { it.symbol == currencyParts[0] }
        val target = allCurrencies.find { it.symbol == currencyParts[1] }
        if (base == null || target == null)
            throw Exception("Can't find base or target for Currencies string invalid $str")

        return base to target
    }

    private fun getTargetSymbolAndUseInverse(
        base: Currency,
        target: Currency
    ) = if (base.symbol.equals("EUR", true)) {
        target.symbol to false
    } else {
        base.symbol to true
    }
}