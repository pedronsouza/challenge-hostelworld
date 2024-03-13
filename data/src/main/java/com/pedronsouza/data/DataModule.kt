package com.pedronsouza.data

import com.pedronsouza.domain.repositories.PropertyRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DataModule = module {
    singleOf(::PropertyRepositoryImpl) { bind<PropertyRepository>() }
}