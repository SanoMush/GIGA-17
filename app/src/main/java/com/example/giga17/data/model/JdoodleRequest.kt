package com.example.giga17.data.model

import kotlinx.serialization.Serializable

@Serializable
data class JdoodleRequest(
    val script: String,
    val language: String,
    val versionIndex: String,
    val clientId: String,
    val clientSecret: String
)
