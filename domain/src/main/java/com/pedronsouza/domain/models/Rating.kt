package com.pedronsouza.domain.models

data class Rating(
    val security: Int,
    val location: Int,
    val staff: Int,
    val clean: Int,
    val facilities: Int,
    val average: Int,
    val overall: Int
)