package com.pedronsouza.shared

import com.pedronsouza.domain.ContentParser
import com.pedronsouza.domain.values.HtmlContent
import com.pedronsouza.shared.parsers.HtmlToTexContentParser
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val SharedModule = module {
    factoryOf(::HtmlToTexContentParser) { bind<ContentParser<HtmlContent, String>>() }
}