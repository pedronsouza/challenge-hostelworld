package com.pedronsouza.domain

import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val DomainModule = module {
    factoryOf(::LoadPropertiesUseCase)
}