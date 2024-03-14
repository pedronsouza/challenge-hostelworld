package com.pedronsouza.domain

interface ContentParser<InValue, OutValue> {
    fun parse(value: InValue): OutValue
}