package com.omarahmedd.currencyapp.domain

import com.omarahmedd.currencyapp.model.Currency
import javax.inject.Inject

class SetupConversionUseCase @Inject constructor(
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase
) {

    suspend fun invoke(): UCResult<List<Currency>> {
        return try {
            val currencies = getCurrencyUseCase.invoke().successOrThrow()
            getExchangeRatesUseCase.invoke().successOrThrow()
            UCResult.Success(currencies)
        } catch (t: Throwable) {
            UCResult.Error(t)
        }
    }

}