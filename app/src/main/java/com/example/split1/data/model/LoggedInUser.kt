package com.example.split1.data.model

import java.net.URL

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val profilePictureURL: String
)