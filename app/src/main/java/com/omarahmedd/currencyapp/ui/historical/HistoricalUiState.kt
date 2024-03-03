package com.omarahmedd.currencyapp.ui.historical

import com.omarahmedd.currencyapp.model.HistoricalState

sealed class HistoricalUiState

data class SuccessUiState(val historicalState: HistoricalState) : HistoricalUiState()
data object LoadingUiState : HistoricalUiState()
data class ErrorUiState(val errorMessage: String) : HistoricalUiState()