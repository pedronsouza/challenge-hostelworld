package com.pedronsouza.domain.models

@JvmInline
value class RemoteResource(val url: String) {
    override fun toString(): String = url
}