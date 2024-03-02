package com.omarahmedd.currencyapp.data

import com.omarahmedd.currencyapp.model.Currency

interface ExchangeRateRepository{

    suspend fun getExchangeRates()

    fun getExchangeRate(base: Currency?, target: Currency?): Double

}