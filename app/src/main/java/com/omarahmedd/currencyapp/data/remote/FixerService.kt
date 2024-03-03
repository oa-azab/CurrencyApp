package com.omarahmedd.currencyapp.data.remote

import com.omarahmedd.currencyapp.data.remote.model.CurrencyResponse
import com.omarahmedd.currencyapp.data.remote.model.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FixerService {

    companion object {
        private const val API_KEY = "f87f23dd59525378c2b97f441576bd5d"
    }

    @GET("symbols")
    suspend fun getCurrencies(
        @Query("access_key") apiKey: String = API_KEY
    ): Response<CurrencyResponse>

    @GET("latest")
    suspend fun getExchangeRates(
        @Query("access_key") apiKey: String = API_KEY
    ): Response<ExchangeRateResponse>

    @GET("{day}")
    suspend fun getExchangeRateAt(
        @Path("day") day: String,
        @Query("access_key") apiKey: String = API_KEY,
        @Query("symbols") target: String,
    ): Response<ExchangeRateResponse>

}