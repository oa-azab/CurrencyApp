package com.omarahmedd.currencyapp.data.fake

import com.omarahmedd.currencyapp.data.ExchangeRateRepository
import com.omarahmedd.currencyapp.model.Currency
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FakeExchangeRateRepository @Inject constructor() : ExchangeRateRepository {

    override suspend fun getExchangeRates() {
        delay(1000)
    }

    override fun getExchangeRate(base: Currency?, target: Currency?): Double {
        val key = "${base?.symbol}-${target?.symbol}"
        return when (key) {
            "USD-EUR" -> 0.922216
            "EUR-USD" -> 1.0843452
            "USD-EGP" -> 30.90377
            "EGP-USD" -> 0.032358511
            "EUR-EGP" -> 33.388701
            "EGP-EUR" -> 0.029950252
            else -> 0.0
        }
    }

    override suspend fun getExchangeRateAt(day: String, targetSymbol: String): Double {
        delay(300)
        return Random.nextDouble(1.2, 1.7)
    }

}