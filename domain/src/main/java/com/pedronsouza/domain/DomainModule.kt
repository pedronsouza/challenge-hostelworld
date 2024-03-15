package com.pedronsouza.domain

import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import com.pedronsouza.domain.useCases.LoadPropertiesUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.core.module.dsl.bind

val DomainModule = module {
    factoryOf(::LoadPropertiesUseCaseImpl) { bind<LoadPropertiesUseCase>() }
}