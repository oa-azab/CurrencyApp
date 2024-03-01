package com.omarahmedd.currencyapp.data.remote

import com.omarahmedd.currencyapp.data.remote.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerService {

    @GET("symbols")
    suspend fun getCurrencies(
        @Query("access_key") apiKey: String = "f87f23dd59525378c2b97f441576bd5d"
    ): Response<CurrencyResponse>

}