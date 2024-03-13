package com.pedronsouza.domain

abstract class ObjectMapper<I, O>() {
    abstract fun transform(inputData: I): O
}