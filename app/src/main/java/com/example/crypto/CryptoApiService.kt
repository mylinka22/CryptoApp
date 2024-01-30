package com.example.crypto

import retrofit2.Call
import retrofit2.http.GET

interface CryptoApiService {
    @GET("simple/price?ids=bitcoin&vs_currencies=usd")
    fun getBitcoinPrice(): Call<BitcoinResponse>
}
