package com.omarahmedd.currencyapp.di

import com.omarahmedd.currencyapp.data.ExchangeRateRepository
import com.omarahmedd.currencyapp.data.ExchangeRateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DiBinds {

    @Binds
    abstract fun exchangeRates(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository

}