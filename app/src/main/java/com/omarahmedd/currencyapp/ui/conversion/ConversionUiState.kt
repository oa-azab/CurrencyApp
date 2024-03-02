package com.omarahmedd.currencyapp.ui.conversion

import com.omarahmedd.currencyapp.model.ConversionState
import com.omarahmedd.currencyapp.model.Currency


sealed class ConversionUiState

data class SuccessUiState(
    val currencies: List<Currency>,
    val conversionState: ConversionState
) : ConversionUiState()

data object LoadingUiState : ConversionUiState()
data class ErrorUiState(val errorMessage: String) : ConversionUiState()