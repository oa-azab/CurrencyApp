package com.omarahmedd.currencyapp.model

data class HistoricalState(
    val base: Currency,
    val target: Currency,
    val entries: List<HistoricalEntry>
)