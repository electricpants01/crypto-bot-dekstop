package model

data class Crypto(
    val symbol: String?,
    val price: Double?,
    val percentage: String? = null,
    val rsi: Double?
)