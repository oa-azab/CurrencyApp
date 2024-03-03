package com.omarahmedd.currencyapp.ui.historical

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarahmedd.currencyapp.domain.GetHistoricalDataUseCase
import com.omarahmedd.currencyapp.domain.UCResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHistoricalDataUseCase: GetHistoricalDataUseCase
) : ViewModel() {

    private val currencies: String? = savedStateHandle["currencies"]

    private val _state = MutableStateFlow<HistoricalUiState>(LoadingUiState)
    val state: StateFlow<HistoricalUiState> = _state

    init {
        Log.d("HistoricalViewModel", "currencies = $currencies")
        loadHistoricalData()
    }

    fun loadHistoricalData() {
        viewModelScope.launch {
            _state.value = LoadingUiState
            val result = getHistoricalDataUseCase.invoke(currencies.orEmpty())
            when (result) {
                is UCResult.Success -> {
                    Log.d("HistoricalViewModel", "Success")
                    result.data.entries.forEach {
                        Log.d("HistoricalViewModel", it.toString())
                    }
                    _state.value = SuccessUiState(result.data)
                }

                is UCResult.Error -> {
                    Log.d("HistoricalViewModel", "Error")
                    Log.w("HistoricalViewModel", result.throwable)
                    _state.value = ErrorUiState(result.throwable.message.toString())
                }
            }
        }
    }

}