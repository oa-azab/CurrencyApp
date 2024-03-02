package com.omarahmedd.currencyapp.domain

import com.omarahmedd.currencyapp.data.ExchangeRateRepository
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repository: ExchangeRateRepository
) {

    suspend fun invoke(): UCResult<Unit> {
        return try {
            repository.getExchangeRates()
            UCResult.Success(Unit)
        } catch (t: Throwable) {
            UCResult.Error(t)
        }
    }
}