package com.omarahmedd.currencyapp.domain

import com.omarahmedd.currencyapp.FakeExchangeRateRepository
import com.omarahmedd.currencyapp.model.ConversionState
import com.omarahmedd.currencyapp.model.Currency
import org.junit.Assert.assertEquals
import org.junit.Test

class ConversionTest {

    companion object {
        val USD = Currency("USD", "US Dollar")
        val EUR = Currency("EUR", "Euro")
        val EGP = Currency("EGP", "Egyptian Pound")
    }

    private val exchangeRepo = FakeExchangeRateRepository()
    private val conversion = Conversion(exchangeRepo)

    @Test
    fun testChangeSource() {
        val initialState = ConversionState(null, EUR, "1.0", "0.0")
        val newState = conversion.changeSource(USD, initialState)

        val expectedTarget = 1.0 * exchangeRepo.getExchangeRate(USD, EUR)
        assertEquals(newState.source, USD)
        assertEquals(newState.targetAmount, String.format("%.2f", expectedTarget))
    }

    @Test
    fun testChangeTarget() {
        val initialState = ConversionState(USD, EGP, "1.0", "0.0")
        val newState = conversion.changeTarget(EUR, initialState)

        val expectedTarget = 1.0 * exchangeRepo.getExchangeRate(USD, EUR)
        assertEquals(newState.target, EUR)
        assertEquals(newState.targetAmount, String.format("%.2f", expectedTarget))
    }

    @Test
    fun testChangeSourceAmount() {
        val initialState = ConversionState(USD, EUR, "1.0", "0.0")
        val newState = conversion.changeSourceAmount("2.0", initialState)

        val expectedTarget = 2.0 * exchangeRepo.getExchangeRate(USD, EUR)
        assertEquals(newState.sourceAmount, "2.0")
        assertEquals(newState.targetAmount, String.format("%.2f", expectedTarget))
    }

    @Test
    fun testChangeTargetAmount() {
        val initialState = ConversionState(USD, EUR, "1.0", "0.0")
        val newState = conversion.changeTargetAmount("2.0", initialState)

        val expectedSource = 2.0 / exchangeRepo.getExchangeRate(USD, EUR)
        assertEquals(newState.targetAmount, "2.0")
        assertEquals(newState.sourceAmount, String.format("%.2f", expectedSource))
    }

    @Test
    fun testSwapCurrencies() {
        val initialState = ConversionState(USD, EUR, "1.0", "0.0")
        val newState = conversion.swapCurrencies(initialState)

        val expectedTarget = 1.0 * exchangeRepo.getExchangeRate(EUR, USD)
        assertEquals(newState.source, EUR)
        assertEquals(newState.target, USD)
        assertEquals(newState.targetAmount, String.format("%.2f", expectedTarget))
    }

}