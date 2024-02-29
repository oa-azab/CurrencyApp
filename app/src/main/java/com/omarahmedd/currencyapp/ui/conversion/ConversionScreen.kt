package com.omarahmedd.currencyapp.ui.conversion

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omarahmedd.currencyapp.R
import com.omarahmedd.currencyapp.domain.ConversionState
import com.omarahmedd.currencyapp.domain.Currency
import com.omarahmedd.currencyapp.ui.theme.CurrencyAppTheme

@Composable
fun ConversionRoute(
    viewModel: ConversionViewModel = hiltViewModel()
) {
    ConversionScreen(
        conversionState = viewModel.state.collectAsStateWithLifecycle(),
        onSourceCurrencyChange = { viewModel.changeSourceCurrency(it) },
        onTargetCurrencyChange = { viewModel.changeTargetCurrency(it) },
        onSourceAmountChange = { viewModel.changeSourceAmount(it) },
        onTargetAmountChange = { viewModel.changeTargetAmount(it) },
        onSwapCurrency = { viewModel.swapCurrency() }
    )
}

@Composable
fun ConversionScreen(
    conversionState: State<ConversionState>,
    onSourceCurrencyChange: (Currency) -> Unit,
    onTargetCurrencyChange: (Currency) -> Unit,
    onSourceAmountChange: (String) -> Unit,
    onTargetAmountChange: (String) -> Unit,
    onSwapCurrency: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row {

            CurrencyDropDownMenu(
                modifier = Modifier.weight(1f),
                text = conversionState.value.source?.toString() ?: "From",
                onCurrencyChange = onSourceCurrencyChange
            )

            Button(onClick = onSwapCurrency) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_swap_horiz_24),
                    contentDescription = "Swap"
                )
            }

            CurrencyDropDownMenu(
                modifier = Modifier.weight(1f),
                text = conversionState.value.target?.toString() ?: "To",
                onCurrencyChange = onTargetCurrencyChange
            )
        }

        Row {

            OutlinedTextField(
                value = conversionState.value.sourceAmount.toString(),
                onValueChange = { onSourceAmountChange(it) },
                label = { Text("Source Amount") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(48.dp))

            OutlinedTextField(
                value = conversionState.value.targetAmount.toString(),
                onValueChange = { onTargetAmountChange(it) },
                label = { Text("Target Amount") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )

        }

    }
}

@Composable
private fun CurrencyDropDownMenu(
    modifier: Modifier = Modifier,
    text: String,
    onCurrencyChange: (Currency) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = !expanded }
        ) {
            Text(text = text)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false })
        {
            for (currency in Currency.getCurrencies()) {
                DropdownMenuItem(
                    text = { Text(text = currency.toString()) },
                    onClick = {
                        onCurrencyChange(currency)
                        expanded = false
                    }
                )
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
            mutableStateOf(ConversionState(USD, EUR)),
            {},
            {},
            {},
            {},
            {}
        )
    }
}