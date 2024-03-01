package com.omarahmedd.currencyapp.data

import com.omarahmedd.currencyapp.data.remote.CurrencyRemoteSource
import com.omarahmedd.currencyapp.model.Currency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val remoteSource: CurrencyRemoteSource
) {

    private var cache: List<Currency>? = null

    suspend fun getCurrencies(): List<Currency> {
        val mCache = cache
        if (mCache != null) return mCache

        val currencies = remoteSource.getCurrencies()
        cache = currencies

        return currencies
    }

}