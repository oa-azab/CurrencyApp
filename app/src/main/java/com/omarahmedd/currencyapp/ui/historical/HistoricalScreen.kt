package com.omarahmedd.currencyapp.ui.historical

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omarahmedd.currencyapp.model.Currency
import com.omarahmedd.currencyapp.model.HistoricalState
import com.omarahmedd.currencyapp.ui.ErrorView
import com.omarahmedd.currencyapp.ui.LoadingView
import com.omarahmedd.currencyapp.ui.theme.CurrencyAppTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun HistoricalRoute(
    viewModel: HistoricalViewModel = hiltViewModel()
) {
    HistoricalScreen(
        uiState = viewModel.state.collectAsStateWithLifecycle(),
        onRetryClicked = { viewModel.loadHistoricalData() }
    )
}

@Composable
fun HistoricalScreen(
    uiState: State<HistoricalUiState>,
    onRetryClicked: () -> Unit
) {

    when (val state = uiState.value) {
        is SuccessUiState -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = "${state.historicalState.base}\nTo\n${state.historicalState.target}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))


                val entries = state.historicalState.entries
                val chartEntries = mutableListOf<FloatEntry>()
                entries.forEachIndexed { index, historicalEntry ->
                    chartEntries += entryOf(index, historicalEntry.rate)
                }
                val chartEntryModel = entryModelOf(chartEntries)

                val horizontalAxisValueFormatter =
                    AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
                        entries[value.toInt()].day
                    }

                Chart(
                    chart = columnChart(),
                    model = chartEntryModel,
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(
                        valueFormatter = horizontalAxisValueFormatter
                    ),
                )

                Spacer(modifier = Modifier.height(36.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.historicalState.entries) { entry ->
                        Column {
                            Text(
                                text = entry.day,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "1.0 ${state.historicalState.base.symbol} -> " +
                                        String.format("%.2f", entry.rate) +
                                        " ${state.historicalState.target.symbol}"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
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
fun HistoricalScreenPreview() {
    CurrencyAppTheme {
        HistoricalScreen(
            mutableStateOf(
                SuccessUiState(
                    HistoricalState(
                        Currency("USD", ""), Currency("", ""),
                        listOf()
                    )
                )
            ),
            {}
        )
    }
}