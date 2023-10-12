package me.tepen.wheelwander.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public class APIClient {
    // TODO: Change IP Address
    private val BASE_URL : String = "http://192.168.1.97:3000"
    private var retrofit : Retrofit? = null

    public fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}