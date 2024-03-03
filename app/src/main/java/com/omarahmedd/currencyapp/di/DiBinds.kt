package com.omarahmedd.currencyapp.di

import com.omarahmedd.currencyapp.data.CurrencyRepository
import com.omarahmedd.currencyapp.data.ExchangeRateRepository
import com.omarahmedd.currencyapp.data.fake.FakeCurrencyRepository
import com.omarahmedd.currencyapp.data.fake.FakeExchangeRateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DiBinds {

    @Binds
    abstract fun currency(impl: FakeCurrencyRepository): CurrencyRepository

    @Binds
    abstract fun exchangeRates(impl: FakeExchangeRateRepository): ExchangeRateRepository

}