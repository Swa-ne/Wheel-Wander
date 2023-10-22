package me.tepen.wheelwander.models

data class VehicleDetail(val VehicleID: Int,
                         val VehicleType: String,
                         val VehicleBrand: String,
                         val VehicleModel: String,
                         val PlateNumber: String,
                         val Fuel: String,
                         val Description: String,
                         val Location: String,
                         val Longitude: String,
                         val Latitude: String,
                         val Price: Int,
                         val TimeFrame: String,
                         val Listed: String,
                         val TimesRented: Int,
    val UserID: Int,
    val UserName: String)
