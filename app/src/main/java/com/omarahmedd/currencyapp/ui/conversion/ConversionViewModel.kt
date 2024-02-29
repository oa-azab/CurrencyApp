package com.omarahmedd.currencyapp.ui.conversion

import android.util.Log
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
        val value = stringToDouble(amountStr) ?: return
        val newState = Conversion.changeSourceAmount(value, state.value)
        _state.value = newState
    }

    fun changeTargetAmount(amountStr: String) {
        val value = stringToDouble(amountStr) ?: return
        val newState = Conversion.changeTargetAmount(value, state.value)
        _state.value = newState
    }

    fun swapCurrency() {
        val newState = Conversion.swapCurrencies(state.value)
        _state.value = newState
    }

    private fun stringToDouble(str: String): Double? {
        return try {
            str.toDouble()
        } catch (e: NumberFormatException) {
            Log.w("ConversionViewModel", e)
            null
        }
    }
}