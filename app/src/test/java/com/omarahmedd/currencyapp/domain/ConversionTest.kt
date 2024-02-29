package com.omarahmedd.currencyapp.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ConversionTest {

    companion object {
        val USD = Currency("USD", "US Dollar")
        val EUR = Currency("EUR", "Euro")
        val EGP = Currency("EGP", "Egyptian Pound")
    }

    @Test
    fun testChangeSource() {
        val initialState = ConversionState(null, EUR, "1.0", "0.0")
        val newState = Conversion.changeSource(USD, initialState)

        val expectedTarget = 1.0 * ExchangeRate.getRate(USD, EUR)
        assertEquals(newState.source, USD)
        assertEquals(newState.targetAmount, expectedTarget, 0.000001)
    }

    @Test
    fun testChangeTarget() {
        val initialState = ConversionState(USD, EGP, "1.0", "0.0")
        val newState = Conversion.changeTarget(EUR, initialState)

        val expectedTarget = 1.0 * ExchangeRate.getRate(USD, EUR)
        assertEquals(newState.target, EUR)
        assertEquals(newState.targetAmount, expectedTarget, 0.000001)
    }

    @Test
    fun testChangeSourceAmount() {
        val initialState = ConversionState(USD, EUR, "1.0", "0.0")
        val newState = Conversion.changeSourceAmount("2.0", initialState)

        val expectedTarget = 2.0 * ExchangeRate.getRate(USD, EUR)
        assertEquals(newState.sourceAmount, 2.0, 0.000001)
        assertEquals(newState.targetAmount, expectedTarget, 0.000001)
    }

    @Test
    fun testChangeTargetAmount() {
        val initialState = ConversionState(USD, EUR, "1.0", "0.0")
        val newState = Conversion.changeTargetAmount("2.0", initialState)

        val expectedSource = 2.0 / ExchangeRate.getRate(USD, EUR)
        assertEquals(newState.targetAmount, 2.0, 0.000001)
        assertEquals(newState.sourceAmount, expectedSource, 0.000001)
    }

    @Test
    fun testSwapCurrencies() {
        val initialState = ConversionState(USD, EUR, "1.0", "0.0")
        val newState = Conversion.swapCurrencies(initialState)

        val expectedTarget = 1.0 * ExchangeRate.getRate(EUR, USD)
        assertEquals(newState.source, EUR)
        assertEquals(newState.target, USD)
        assertEquals(newState.targetAmount, expectedTarget, 0.000001)
    }

}