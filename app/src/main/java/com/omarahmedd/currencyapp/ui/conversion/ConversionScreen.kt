package com.omarahmedd.currencyapp.ui.conversion

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        when (val state = uiState.value) {
            is SuccessUiState -> {
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
                    modifier = Modifier.padding(top = 16.dp),
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
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Details")
                }
            }

            is LoadingUiState -> {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Loading ... "
                )

                LinearProgressIndicator()
            }

            is ErrorUiState -> {
                Text(text = state.errorMessage)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetryClicked) {
                    Text(text = "Retry")
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyMenu(
    modifier: Modifier = Modifier,
    currencies: List<Currency>,
    text: String,
    onCurrencyChange: (Currency) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var filterTerm by remember { mutableStateOf("") }
    var filteredCurrencies by remember { mutableStateOf(currencies) }

    Box(modifier = modifier) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = !expanded },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                minLines = 3,
                maxLines = 3
            )
        }

        if (expanded) {
            AlertDialog(onDismissRequest = { expanded = false }) {
                Surface(
                    modifier = Modifier.fillMaxSize(0.95f)
                ) {

                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        OutlinedTextField(
                            value = filterTerm,
                            label = { Text("Filter") },
                            onValueChange = { term ->
                                filterTerm = term
                                filteredCurrencies = if (term.isBlank()) {
                                    currencies
                                } else {
                                    currencies.filter { it.toString().contains(term, true) }
                                }
                            }
                        )

                        LazyColumn(modifier = Modifier.weight(1f)) {

                            items(filteredCurrencies) { currency ->
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .clickable {
                                            onCurrencyChange(currency)
                                            expanded = false
                                            filterTerm = ""
                                            filteredCurrencies = currencies
                                        },
                                    text = currency.toString()
                                )
                            }
                        }

                        Button(onClick = {
                            expanded = false
                            filterTerm = ""
                            filteredCurrencies = currencies
                        }) {
                            Text(text = "Cancel")
                        }

                    }

                }
            }
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

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun CurrencyMenuPreview() {
    CurrencyAppTheme {
        val USD = Currency("USD", "US Dollar")
        val EUR = Currency("EUR", "Euro")

        CurrencyMenu(
            currencies = listOf(USD, EUR),
            text = "From",
            onCurrencyChange = {}
        )

    }
}