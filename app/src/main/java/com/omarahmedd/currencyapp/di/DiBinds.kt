package com.omarahmedd.currencyapp.di

import com.omarahmedd.currencyapp.data.CurrencyRepository
import com.omarahmedd.currencyapp.data.ExchangeRateRepository
import com.omarahmedd.currencyapp.data.impl.CurrencyRepositoryImpl
import com.omarahmedd.currencyapp.data.impl.ExchangeRateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DiBinds {

    @Binds
    abstract fun currency(impl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    abstract fun exchangeRates(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository

}