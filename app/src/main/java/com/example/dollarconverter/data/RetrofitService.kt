package com.example.dollarconverter.data


import com.example.dollarconverter.data.model.RemoteResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("v6/9ad24e2e5afbf989e638815c/latest/USD" )
    suspend fun getDollarRates(): RemoteResult
}

object RetrofitClient {
    private const val BASE_URL = "https://v6.exchangerate-api.com/"

    val service: RetrofitService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }
}
