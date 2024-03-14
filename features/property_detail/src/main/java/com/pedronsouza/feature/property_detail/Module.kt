package com.pedronsouza.feature.property_detail

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val FeaturePropertyDetailModule = module {
    viewModelOf(::PropertyDetailViewModel)
}