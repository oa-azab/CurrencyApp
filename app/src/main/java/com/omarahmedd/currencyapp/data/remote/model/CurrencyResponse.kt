package com.omarahmedd.currencyapp.data.remote.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CurrencyResponse(

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("symbols")
    val symbols: JsonObject?,

    @SerializedName("error")
    val error: ApiError?
)