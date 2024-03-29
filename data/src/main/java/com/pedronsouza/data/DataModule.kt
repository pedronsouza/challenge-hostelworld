package com.pedronsouza.data

import com.pedronsouza.data.cache.LocalCurrencyDataSourceImpl
import com.pedronsouza.data.dataSources.NetworkMonitorDataSource
import com.pedronsouza.data.dataSources.RemoteNetworkMonitorDataSource
import com.pedronsouza.data.dataSources.RemotePropertyDataSource
import com.pedronsouza.data.internal.ServicesFactory
import com.pedronsouza.data.internal.ServicesFactoryImpl
import com.pedronsouza.data.mappers.GetCurrencyResponseMapper
import com.pedronsouza.data.mappers.GetCurrencyResponseMapperImpl
import com.pedronsouza.data.mappers.PropertyMapper
import com.pedronsouza.data.mappers.PropertyMapperImpl
import com.pedronsouza.data.repositories.CurrencyRepositoryImpl
import com.pedronsouza.data.repositories.NetworkStatsRepositoryImpl
import com.pedronsouza.data.repositories.PropertyRepositoryImpl
import com.pedronsouza.domain.dataSources.LocalCurrencyDataSource
import com.pedronsouza.domain.dataSources.PropertyDataSource
import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.repositories.NetworkStatsRepository
import com.pedronsouza.domain.repositories.PropertyRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DataModule = module {
    singleOf(::ServicesFactoryImpl) { bind<ServicesFactory>() }
    factoryOf(::PropertyRepositoryImpl) { bind<PropertyRepository>() }
    factoryOf(::RemotePropertyDataSource) { bind<PropertyDataSource>() }
    factoryOf(::PropertyMapperImpl) { bind<PropertyMapper>() }
    factoryOf(::CurrencyRepositoryImpl) { bind<CurrencyRepository>() }
    singleOf(::LocalCurrencyDataSourceImpl) { bind<LocalCurrencyDataSource>() }
    factoryOf(::GetCurrencyResponseMapperImpl) { bind<GetCurrencyResponseMapper>() }
    factoryOf(::NetworkStatsRepositoryImpl) { bind<NetworkStatsRepository>() }
    factoryOf(::RemoteNetworkMonitorDataSource) { bind<NetworkMonitorDataSource>() }

}