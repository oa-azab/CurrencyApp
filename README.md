# CurrencyApp

Simple app for currency conversion and listing historical exchange rate between currencies.

App gets currencies and exchange rates from [Fixer.io](https://fixer.io/)


# Architecture

App has 3 Main Packages (Data, Domain, UI) if the app is more complex and these packages could separate modules.

## Data
This package where repositories and data sources live, the classes are responsible for read/write data from sources and map them to app business models.


## Domain
This package contain business logic of the app functionality.
consist of number of UseCases each on serve business function and should return simple result to be displayed by UI layer.


## UI
This is MVVM presentation layer built with Compose UI, where View renders app state exposed by ViewModel.

# Run with fake data
due to fixer.io api free plan requests limit you can run the app with fake repositories from `di.DiBinds`

    @Module
    @InstallIn(SingletonComponent::class)  
    abstract class DiBinds {  
      
    @Binds  
    abstract fun currency(impl: FakeCurrencyRepository): CurrencyRepository  
      
    @Binds  
    abstract fun exchangeRates(impl: FakeExchangeRateRepository): ExchangeRateRepository  
      
    }
