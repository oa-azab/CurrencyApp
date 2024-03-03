package com.omarahmedd.currencyapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omarahmedd.currencyapp.model.Currency


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyMenu(
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

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Loading ... "
        )
        LinearProgressIndicator()
    }
}

@Composable
fun ErrorView(
    errorMessage: String,
    onRetryClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = errorMessage)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetryClicked) {
            Text(text = "Retry")
        }
    }
}