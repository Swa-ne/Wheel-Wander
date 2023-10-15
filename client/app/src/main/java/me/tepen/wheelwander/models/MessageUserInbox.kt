package me.tepen.wheelwander.models

import android.graphics.Bitmap

data class MessageUserInbox (
    val userID: String,
    val username: String,
    var message: String,
    val time: String,
    val avatar: Bitmap
)