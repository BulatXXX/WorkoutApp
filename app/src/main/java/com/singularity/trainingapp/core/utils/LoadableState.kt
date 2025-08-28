package com.singularity.trainingapp.core.utils

interface LoadableState {
    val isLoading: Boolean
    val error: String?
}