// BancoApiService.kt
package com.example.bank.viewmodel

import com.example.bank.model.ApiBanco
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface BancoApiService {
    @GET("banks/v1")
    suspend fun getBancos(): List<ApiBanco>
}

object RetrofitInstance {
    private const val BASE_URL = "https://brasilapi.com.br/api/"

    val api: BancoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BancoApiService::class.java)
    }
}
