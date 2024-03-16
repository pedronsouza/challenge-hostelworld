package com.pedronsouza.domain

import com.pedronsouza.domain.useCases.GetAvailableCurrenciesUseCase
import com.pedronsouza.domain.useCases.GetAvailableCurrenciesUseCaseImpl
import com.pedronsouza.domain.useCases.GetSelectedCurrencyUseCase
import com.pedronsouza.domain.useCases.GetSelectedCurrencyUseCaseImpl
import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import com.pedronsouza.domain.useCases.LoadPropertiesUseCaseImpl
import com.pedronsouza.domain.useCases.SaveSelectedCurrencyUseCase
import com.pedronsouza.domain.useCases.SaveSelectedCurrencyUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val DomainModule = module {
    factoryOf(::LoadPropertiesUseCaseImpl) { bind<LoadPropertiesUseCase>() }
    factoryOf(::GetSelectedCurrencyUseCaseImpl) { bind<GetSelectedCurrencyUseCase>() }
    factoryOf(::GetAvailableCurrenciesUseCaseImpl) { bind<GetAvailableCurrenciesUseCase>() }
    factoryOf(::SaveSelectedCurrencyUseCaseImpl) { bind<SaveSelectedCurrencyUseCase>() }
}