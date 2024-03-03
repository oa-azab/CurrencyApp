package com.omarahmedd.currencyapp.data.impl

import com.omarahmedd.currencyapp.data.CurrencyRepository
import com.omarahmedd.currencyapp.data.remote.CurrencyRemoteSource
import com.omarahmedd.currencyapp.model.Currency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val remoteSource: CurrencyRemoteSource
) : CurrencyRepository {

    private var cache: List<Currency>? = null

    override suspend fun getCurrencies(): List<Currency> {
        val mCache = cache
        if (mCache != null) return mCache

        val currencies = remoteSource.getCurrencies()
        cache = currencies

        return currencies
    }

}