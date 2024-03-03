package com.omarahmedd.currencyapp.data

import com.omarahmedd.currencyapp.model.Currency

interface CurrencyRepository {

    suspend fun getCurrencies(): List<Currency>

}