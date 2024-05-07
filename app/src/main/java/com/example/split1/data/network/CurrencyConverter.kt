package com.example.split1.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyConverter {

    private val currencyApiService = RetrofitClient.create()

    fun convertNisToUsd(nisAmount: Double, callback: (Double) -> Unit) {
        val call = currencyApiService.convertCurrency("ILS", "USD")

        call.enqueue(object : retrofit2.Callback<ConversionResponse> {
            override fun onResponse(call: Call<ConversionResponse>, response: retrofit2.Response<ConversionResponse>) {
                if (response.isSuccessful) {
                    val exchangeRate = response.body()?.exchangeRates?.get("USD") ?: 0.0
                    val convertedAmount = nisAmount * exchangeRate
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

    fun convertUsdToNis(usdAmount: Double, callback: (Double) -> Unit) {
        val call = currencyApiService.convertCurrency("USD", "ILS")

        call.enqueue(object : retrofit2.Callback<ConversionResponse> {
            override fun onResponse(call: Call<ConversionResponse>, response: retrofit2.Response<ConversionResponse>) {
                if (response.isSuccessful) {
                    val exchangeRate = response.body()?.exchangeRates?.get("ILS") ?: 0.0
                    val convertedAmount = usdAmount * exchangeRate
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

    private const val BASE_URL = "https://freecurrencyapi.com/api/"
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

    @GET("latest")
    fun convertCurrency(
        @Query("base_currency") fromCurrency: String,
        @Query("currencies") toCurrency: String,
    ): Call<ConversionResponse>
}

data class ConversionResponse(
    @SerializedName("data") val exchangeRates: Map<String, Double>
)