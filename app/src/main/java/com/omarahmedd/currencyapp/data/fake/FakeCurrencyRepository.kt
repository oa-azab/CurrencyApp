package com.omarahmedd.currencyapp.data.fake

import com.omarahmedd.currencyapp.data.CurrencyRepository
import com.omarahmedd.currencyapp.data.remote.CurrencyRemoteSource
import com.omarahmedd.currencyapp.model.Currency
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCurrencyRepository @Inject constructor() : CurrencyRepository {

    companion object {
        val USD = Currency("USD", "US Dollar")
        val EUR = Currency("EUR", "Euro")
        val EGP = Currency("EGP", "Egyptian Pound")
    }

    override suspend fun getCurrencies(): List<Currency> {
        delay(1000)
        return listOf(USD, EUR, EGP)
    }

}