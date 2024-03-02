package com.omarahmedd.currencyapp.data

import android.util.Log
import com.omarahmedd.currencyapp.data.remote.ExchangeRateRemoteSource
import com.omarahmedd.currencyapp.model.Currency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepositoryImpl @Inject constructor(
    private val remoteSource: ExchangeRateRemoteSource
) : ExchangeRateRepository {

    private val cache = mutableMapOf<String, Double>()

    override suspend fun getExchangeRates() {
        val rates = remoteSource.getExchangeRates()
        cache.clear()
        for (entry in rates) {
            cache[entry.key] = entry.value
        }
    }

    override fun getExchangeRate(base: Currency?, target: Currency?): Double {
        Log.d("ExchangeRateRepository", "base=$base , target=$target")
        if (base == null || target == null) return 0.0

        val key = "${base.symbol}-${target.symbol}"
        val rate = cache[key]
        Log.d("ExchangeRateRepository", "key=$key -> rate=$rate")

        return rate ?: 0.0
    }

}