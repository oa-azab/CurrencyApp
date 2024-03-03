package com.omarahmedd.currencyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.omarahmedd.currencyapp.ui.conversion.ConversionRoute
import com.omarahmedd.currencyapp.ui.historical.HistoricalRoute
import com.omarahmedd.currencyapp.ui.theme.CurrencyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "conversion") {
        composable("conversion") {
            ConversionRoute(
                navToHistorical = { currencies -> navController.navigate("historical/$currencies") }
            )
        }
        composable(
            "historical/{currencies}",
            arguments = listOf(
                navArgument("currencies") { type = NavType.StringType },
            )
        ) {
            HistoricalRoute()
        }

    }
}