package com.example.crypto

data class BitcoinResponse(
    val bitcoin: BitcoinData
)
data class BitcoinData(
    val usd: Double
)