package com.omarahmedd.currencyapp.ui.conversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarahmedd.currencyapp.domain.Conversion
import com.omarahmedd.currencyapp.domain.ConversionState
import com.omarahmedd.currencyapp.domain.GetCurrencyUseCase
import com.omarahmedd.currencyapp.domain.UCResult
import com.omarahmedd.currencyapp.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ConversionUiState>(LoadingUiState)
    val state: StateFlow<ConversionUiState> = _state

    init {
        getCurrencies()
    }

    fun getCurrencies() {
        viewModelScope.launch {
            _state.value = LoadingUiState
            when (val result = getCurrencyUseCase.invoke()) {
                is UCResult.Success -> {
                    _state.value = SuccessUiState(result.data, ConversionState())
                }

                is UCResult.Error -> {
                    _state.value = ErrorUiState(result.throwable.message.orEmpty())
                }
            }

        }
    }

    fun changeSourceCurrency(currency: Currency) {
        val oldState = getSuccessState()
        val newConversionState = Conversion.changeSource(currency, oldState.conversionState)
        _state.value = oldState.copy(conversionState = newConversionState)
    }

    fun changeTargetCurrency(currency: Currency) {
        val oldState = getSuccessState()
        val newConversionState = Conversion.changeTarget(currency, oldState.conversionState)
        _state.value = oldState.copy(conversionState = newConversionState)
    }

    fun changeSourceAmount(amountStr: String) {
        val oldState = getSuccessState()
        val newConversionState = Conversion.changeSourceAmount(amountStr, oldState.conversionState)
        _state.value = oldState.copy(conversionState = newConversionState)
    }

    fun changeTargetAmount(amountStr: String) {
        val oldState = getSuccessState()
        val newConversionState = Conversion.changeTargetAmount(amountStr, oldState.conversionState)
        _state.value = oldState.copy(conversionState = newConversionState)
    }

    fun swapCurrency() {
        val oldState = getSuccessState()
        val newConversionState = Conversion.swapCurrencies(oldState.conversionState)
        _state.value = oldState.copy(conversionState = newConversionState)
    }

    private fun getSuccessState(): SuccessUiState {
        return when (val value = state.value) {
            is SuccessUiState -> value
            else -> SuccessUiState(listOf(), ConversionState())
        }
    }

}