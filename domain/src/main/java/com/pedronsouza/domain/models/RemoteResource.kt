package com.pedronsouza.domain.models

import java.io.Serializable

@JvmInline
value class RemoteResource(val url: String) : Serializable {
    override fun toString(): String = url
}