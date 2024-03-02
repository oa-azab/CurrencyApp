package com.omarahmedd.currencyapp.data.remote.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(

    @SerializedName("rates")
    val rates: JsonObject?,

    @SerializedName("error")
    val error: ApiError?

)