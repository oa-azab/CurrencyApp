package com.omarahmedd.currencyapp.ui.conversion

import androidx.lifecycle.ViewModel
import com.omarahmedd.currencyapp.domain.Conversion
import com.omarahmedd.currencyapp.domain.ConversionState
import com.omarahmedd.currencyapp.domain.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ConversionState())
    val state: StateFlow<ConversionState> = _state

    fun changeSourceCurrency(currency: Currency) {
        val newState = Conversion.changeSource(currency, state.value)
        _state.value = newState
    }

    fun changeTargetCurrency(currency: Currency) {
        val newState = Conversion.changeTarget(currency, state.value)
        _state.value = newState
    }

    fun changeSourceAmount(amountStr: String) {
        val newState = Conversion.changeSourceAmount(amountStr, state.value)
        _state.value = newState
    }

    fun changeTargetAmount(amountStr: String) {
        val newState = Conversion.changeTargetAmount(amountStr, state.value)
        _state.value = newState
    }

    fun swapCurrency() {
        val newState = Conversion.swapCurrencies(state.value)
        _state.value = newState
    }

}