package com.pedronsouza.feature.property_list

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val PropertyListFeatureModule = module {
    viewModelOf(::PropertyListViewModel)
}