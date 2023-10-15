package me.tepen.wheelwander.models

import android.graphics.Bitmap

data class Vehicles(
    var ID : String,
    var type : String,
    var name : String,
    var mainImage : Bitmap,
    var rate : String
)
