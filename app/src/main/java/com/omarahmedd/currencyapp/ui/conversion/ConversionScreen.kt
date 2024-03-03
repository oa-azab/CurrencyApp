package com.omarahmedd.currencyapp.ui.conversion

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omarahmedd.currencyapp.R
import com.omarahmedd.currencyapp.model.ConversionState
import com.omarahmedd.currencyapp.model.Currency
import com.omarahmedd.currencyapp.ui.CurrencyMenu
import com.omarahmedd.currencyapp.ui.ErrorView
import com.omarahmedd.currencyapp.ui.LoadingView
import com.omarahmedd.currencyapp.ui.theme.CurrencyAppTheme

@Composable
fun ConversionRoute(
    viewModel: ConversionViewModel = hiltViewModel(),
    navToHistorical: (String) -> Unit
) {
    ConversionScreen(
        uiState = viewModel.state.collectAsStateWithLifecycle(),
        onSourceCurrencyChange = { viewModel.changeSourceCurrency(it) },
        onTargetCurrencyChange = { viewModel.changeTargetCurrency(it) },
        onSourceAmountChange = { viewModel.changeSourceAmount(it) },
        onTargetAmountChange = { viewModel.changeTargetAmount(it) },
        onSwapCurrency = { viewModel.swapCurrency() },
        onRetryClicked = { viewModel.getCurrencies() },
        onDetailsClicked = navToHistorical
    )
}

@Composable
fun ConversionScreen(
    uiState: State<ConversionUiState>,
    onSourceCurrencyChange: (Currency) -> Unit,
    onTargetCurrencyChange: (Currency) -> Unit,
    onSourceAmountChange: (String) -> Unit,
    onTargetAmountChange: (String) -> Unit,
    onSwapCurrency: () -> Unit,
    onRetryClicked: () -> Unit,
    onDetailsClicked: (String) -> Unit
) {

    when (val state = uiState.value) {
        is SuccessUiState -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CurrencyMenu(
                        modifier = Modifier.weight(1f),
                        currencies = state.currencies,
                        text = state.conversionState.source?.toString() ?: "From",
                        onCurrencyChange = onSourceCurrencyChange
                    )

                    Button(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = onSwapCurrency,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_swap_horiz_24),
                            contentDescription = "Swap"
                        )
                    }

                    CurrencyMenu(
                        modifier = Modifier.weight(1f),
                        currencies = state.currencies,
                        text = state.conversionState.target?.toString() ?: "To",
                        onCurrencyChange = onTargetCurrencyChange
                    )
                }

                Row(modifier = Modifier.padding(top = 16.dp)) {

                    OutlinedTextField(
                        value = state.conversionState.sourceAmount,
                        onValueChange = { onSourceAmountChange(it) },
                        label = { Text("Source Amount") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(68.dp))

                    OutlinedTextField(
                        value = state.conversionState.targetAmount,
                        onValueChange = { onTargetAmountChange(it) },
                        label = { Text("Target Amount") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.weight(1f)
                    )

                }

                Text(
                    modifier = Modifier.padding(vertical = 24.dp),
                    text = state.conversionState.message,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = {
                        val base = state.conversionState.source?.symbol
                        val target = state.conversionState.target?.symbol
                        val currencies = "$base-$target"
                        onDetailsClicked(currencies)
                    },
                    enabled = state.conversionState.message.isBlank(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Details")
                }
            }
        }

        is LoadingUiState -> {
            LoadingView()
        }

        is ErrorUiState -> {
            ErrorView(
                errorMessage = state.errorMessage,
                onRetryClicked = onRetryClicked
            )
        }
    }

}


@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun ConversionScreenPreview() {
    CurrencyAppTheme {
        val USD = Currency("USD", "US Dollar")
        val EUR = Currency("EUR", "Euro")

        ConversionScreen(
            mutableStateOf(SuccessUiState(listOf(USD, EUR), ConversionState(USD, EUR))),
            {},
            {},
            {},
            {},
            {},
            {},
            {}
        )
    }
}