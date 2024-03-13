package com.pedronsouza.data

import com.pedronsouza.data.dataSources.RemotePropertyDataSource
import com.pedronsouza.data.internal.ServicesFactory
import com.pedronsouza.data.mappers.PropertyObjectMapper
import com.pedronsouza.data.repositories.PropertyRepositoryImpl
import com.pedronsouza.domain.dataSources.PropertyDataSource
import com.pedronsouza.domain.repositories.PropertyRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DataModule = module {
    singleOf(::ServicesFactory)
    factoryOf(::PropertyRepositoryImpl) { bind<PropertyRepository>() }
    factoryOf(::RemotePropertyDataSource) { bind<PropertyDataSource>() }
    factoryOf(::PropertyObjectMapper)
}