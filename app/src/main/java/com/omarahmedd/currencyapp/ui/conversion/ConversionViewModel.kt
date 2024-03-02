package com.omarahmedd.currencyapp.ui.conversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarahmedd.currencyapp.domain.Conversion
import com.omarahmedd.currencyapp.model.ConversionState
import com.omarahmedd.currencyapp.domain.SetupConversionUseCase
import com.omarahmedd.currencyapp.domain.UCResult
import com.omarahmedd.currencyapp.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val setupConversionUseCase: SetupConversionUseCase,
    private val conversion: Conversion
) : ViewModel() {

    private val _state = MutableStateFlow<ConversionUiState>(LoadingUiState)
    val state: StateFlow<ConversionUiState> = _state

    init {
        getCurrencies()
    }

    fun getCurrencies() {
        viewModelScope.launch {
            _state.value = LoadingUiState
            when (val result = setupConversionUseCase.invoke()) {
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
        val newConversionState = conversion.changeSource(currency, oldState.conversionState)
        _state.value = oldState.copy(conversionState = newConversionState)
    }

    fun changeTargetCurrency(currency: Currency) {
        val oldState = getSuccessState()
        val newConversionState = conversion.changeTarget(currency, oldState.conversionState)
        _state.value = oldState.copy(conversionState = newConversionState)
    }

    fun changeSourceAmount(amountStr: String) {
        val oldState = getSuccessState()
        val newConversionState = conversion.changeSourceAmount(amountStr, oldState.conversionState)
        _state.value = oldState.copy(conversionState = newConversionState)
    }

    fun changeTargetAmount(amountStr: String) {
        val oldState = getSuccessState()
        val newConversionState = conversion.changeTargetAmount(amountStr, oldState.conversionState)
        _state.value = oldState.copy(conversionState = newConversionState)
    }

    fun swapCurrency() {
        val oldState = getSuccessState()
        val newConversionState = conversion.swapCurrencies(oldState.conversionState)
        _state.value = oldState.copy(conversionState = newConversionState)
    }

    private fun getSuccessState(): SuccessUiState {
        return when (val value = state.value) {
            is SuccessUiState -> value
            else -> SuccessUiState(listOf(), ConversionState())
        }
    }

}