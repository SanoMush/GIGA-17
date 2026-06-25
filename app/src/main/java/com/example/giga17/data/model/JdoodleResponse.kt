package com.example.giga17.data.model

import kotlinx.serialization.Serializable

@Serializable
data class JdoodleResponse(
    val output: String? = null,
    val statusCode: Int? = null,
    val memory: String? = null,
    val cpuTime: String? = null,
    val error: String? = null
)
