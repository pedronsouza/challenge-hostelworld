package com.pedronsouza.domain

interface ObjectMapper<I, O> {
    fun transform(inputData: I): O
}