package com.omarahmedd.currencyapp.data.remote

import com.google.gson.JsonObject
import com.omarahmedd.currencyapp.model.Currency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRemoteSource @Inject constructor(
    private val service: FixerService
) {

    suspend fun getCurrencies(): List<Currency> {
        val response = service.getCurrencies()
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            throw Exception(
                "response code = ${response.code()}, message = ${response.errorBody().toString()}"
            )
        }

        if (body.symbols == null) {
            throw Exception("api error code = ${body.error?.code}, info = ${body.error?.info}")
        }

        val list = parseSymbols(body.symbols)
        if(list.isEmpty()){
            throw Exception("symbols are empty object = ${body.symbols}")
        }

        return list
    }


    private fun parseSymbols(jsonObject: JsonObject?): List<Currency> {
        if (jsonObject == null) return emptyList()

        val mutableList = mutableListOf<Currency>()
        jsonObject.entrySet().forEach { entry ->
            mutableList.add(Currency(entry.key, entry.value.asString))
        }
        return mutableList.toList()
    }

}