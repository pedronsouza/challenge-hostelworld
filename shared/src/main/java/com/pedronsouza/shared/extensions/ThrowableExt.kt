package com.pedronsouza.shared.extensions

fun Throwable.getRootCause(): Throwable? {
    if (cause == null)
        return this

    var rootCause: Throwable? = this

    while (rootCause?.cause != null && rootCause != rootCause.cause) {
        rootCause = rootCause.cause
    }

    return rootCause
}