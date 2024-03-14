package com.pedronsouza.shared

import com.pedronsouza.domain.ContentParser
import com.pedronsouza.domain.ObjectMapper
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.values.HtmlContent
import com.pedronsouza.shared.components.models.PropertyListItem
import com.pedronsouza.shared.mappers.PropertyListItemObjectMapper
import com.pedronsouza.shared.navigation.RouteFactory
import com.pedronsouza.shared.navigation.RouteFactoryImpl
import com.pedronsouza.shared.parsers.HtmlToTexContentParser
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val SharedModule = module {
    factoryOf(::HtmlToTexContentParser) { bind<ContentParser<HtmlContent, String>>() }
    factoryOf(::RouteFactoryImpl) { bind<RouteFactory>() }
    factoryOf(::PropertyListItemObjectMapper) { bind<ObjectMapper<List<Property>, List<PropertyListItem>>>() }
}