package com.pedronsouza.domain

import com.pedronsouza.domain.useCases.GetAvailableCurrenciesUseCase
import com.pedronsouza.domain.useCases.GetAvailableCurrenciesUseCaseImpl
import com.pedronsouza.domain.useCases.GetPropertyByIdUseCase
import com.pedronsouza.domain.useCases.GetPropertyByIdUseCaseImpl
import com.pedronsouza.domain.useCases.GetSelectedCurrencyUseCase
import com.pedronsouza.domain.useCases.GetSelectedCurrencyUseCaseImpl
import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import com.pedronsouza.domain.useCases.LoadPropertiesUseCaseImpl
import com.pedronsouza.domain.useCases.SwitchSelectedCurrencyUseCase
import com.pedronsouza.domain.useCases.SwitchSelectedCurrencyUseCaseImpl
import com.pedronsouza.domain.useCases.TrackNetworkResponsesUseCase
import com.pedronsouza.domain.useCases.TrackNetworkResponsesUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val DomainModule = module {
    factory<LoadPropertiesUseCase> { LoadPropertiesUseCaseImpl(get(), get()) }
    factoryOf(::GetPropertyByIdUseCaseImpl){ bind<GetPropertyByIdUseCase>() }

    factoryOf(::GetSelectedCurrencyUseCaseImpl) { bind<GetSelectedCurrencyUseCase>() }
    factoryOf(::GetAvailableCurrenciesUseCaseImpl) { bind<GetAvailableCurrenciesUseCase>() }
    factoryOf(::SwitchSelectedCurrencyUseCaseImpl) { bind<SwitchSelectedCurrencyUseCase>() }
    factoryOf(::TrackNetworkResponsesUseCaseImpl) { bind<TrackNetworkResponsesUseCase>() }
}