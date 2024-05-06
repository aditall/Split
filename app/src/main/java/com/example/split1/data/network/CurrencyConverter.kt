package com.example.split1.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName
import okhttp3.Interceptor
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Callback
import retrofit2.Response

class CurrencyConverter {

    private val currencyApiService = RetrofitClient.create()

    fun convertNisToUsd(nisAmount: Double, callback: (Double) -> Unit) {
        val call = currencyApiService.convertNisToUsd(amount = nisAmount)

        call.enqueue(object : Callback<ConversionResponse> {
            override fun onResponse(call: Call<ConversionResponse>, response: Response<ConversionResponse>) {
                if (response.isSuccessful) {
                    val convertedAmount = response.body()?.convertedAmount ?: 0.0
                    callback(convertedAmount)
                } else {
                    // Handle API error
                    callback(0.0)
                }
            }

            override fun onFailure(call: Call<ConversionResponse>, t: Throwable) {
                // Handle network error
                callback(0.0)
            }
        })
    }
}


object RetrofitClient {

    private const val BASE_URL = "https://freecurrencyapi.com/api/v1/"
    private const val API_KEY = "fca_live_q8SVSwH70sZEhSQv6GNwrnKHzShZtKEAw5W7NAea"  // Replace with your actual API key

    fun create(): CurrencyApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())  // Include custom OkHttpClient for API key handling
            .build()

        return retrofit.create(CurrencyApiService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val modifiedUrl = originalRequest.url().newBuilder()
                .addQueryParameter("apikey", API_KEY)
                .build()

            val modifiedRequest = originalRequest.newBuilder()
                .url(modifiedUrl)
                .build()

            chain.proceed(modifiedRequest)
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }
}


interface CurrencyApiService {

    @GET("convert")
    fun convertNisToUsd(
        @Query("from") fromCurrency: String = "NIS",
        @Query("to") toCurrency: String = "USD",
        @Query("amount") amount: Double
    ): Call<ConversionResponse>
}
data class ConversionResponse(
    @SerializedName("from") val fromCurrency: String,
    @SerializedName("to") val toCurrency: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("converted") val convertedAmount: Double
)