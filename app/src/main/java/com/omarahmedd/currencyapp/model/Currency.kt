package com.omarahmedd.currencyapp.model

data class Currency(
    val symbol: String,
    val name: String
) {

    override fun toString(): String {
        return "$symbol - $name"
    }

}