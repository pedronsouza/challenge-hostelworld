package com.pedronsouza.domain.values

@JvmInline
value class HtmlContent(private val content: String) {
    override fun toString(): String = content
}