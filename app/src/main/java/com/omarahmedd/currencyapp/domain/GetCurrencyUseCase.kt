package com.omarahmedd.currencyapp.domain

import com.omarahmedd.currencyapp.data.CurrencyRepository
import com.omarahmedd.currencyapp.model.Currency
import javax.inject.Inject

class GetCurrencyUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {

    suspend fun invoke(): UCResult<List<Currency>> {
        return try {
            val currencies = repository.getCurrencies()
            UCResult.Success(currencies)
        } catch (t: Throwable) {
            UCResult.Error(t)
        }
    }

}