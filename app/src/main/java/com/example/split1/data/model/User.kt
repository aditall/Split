package com.example.split1.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val userId: String? = null, val email: String? = null) {
}
