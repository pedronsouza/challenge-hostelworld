package com.pedronsouza.domain.mappers

interface ObjectMapper<I, O> {
    fun transform(inputData: I): O
}