package com.singularity.trainingapp.core.network

import kotlinx.serialization.json.Json

object JSONProvider {
    val json: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        coerceInputValues = true
        isLenient = true
    }
}