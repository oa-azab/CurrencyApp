package com.omarahmedd.currencyapp.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRemoteSource @Inject constructor(
    private val service: FixerService
) {

    companion object {
        private const val DEFAULT_SYMBOL = "EUR"
    }

    suspend fun getExchangeRates(): Map<String, Double> {
        val response = service.getExchangeRates()
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            throw Exception(
                "response code = ${response.code()}, message = ${response.errorBody().toString()}"
            )
        }

        if (body.rates == null) {
            throw Exception("api error code = ${body.error?.code}, info = ${body.error?.info}")
        }

        val base = DEFAULT_SYMBOL
        val map = mutableMapOf<String, Double>()
        body.rates.entrySet().forEach { entry ->
            val target = entry.key
            val rate = entry.value.asDouble

            val key1 = "$base-$target"
            val key2 = "$target-$base"

            map[key1] = rate
            map[key2] = 1 / rate
        }

        return map
    }

    suspend fun getExchangeRateAt(day: String, targetSymbol: String): Double {
        val response = service.getExchangeRateAt(day = day, target = targetSymbol)
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            throw Exception(
                "response code = ${response.code()}, message = ${response.errorBody().toString()}"
            )
        }

        if (body.rates == null) {
            throw Exception("api error code = ${body.error?.code}, info = ${body.error?.info}")
        }

        val rate = body.rates.get(targetSymbol).asDouble
        return rate
    }

}