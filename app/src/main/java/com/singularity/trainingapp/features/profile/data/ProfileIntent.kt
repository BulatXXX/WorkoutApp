package com.singularity.trainingapp.features.profile.data

sealed interface ProfileIntent {
    data object Refresh : ProfileIntent
}