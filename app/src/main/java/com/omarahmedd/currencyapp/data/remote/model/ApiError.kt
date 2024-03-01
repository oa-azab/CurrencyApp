package com.omarahmedd.currencyapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("code")
    val code: Int?,

    @SerializedName("info")
    val info: String?,
)
