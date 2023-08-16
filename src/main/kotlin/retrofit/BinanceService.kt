package retrofit

import model.PercentageCrypto
import model.PriceCrypto
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceService {

    @GET("api/v3/klines")
    suspend fun getRsiCrypto(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String = "15m",
        @Query("limit") limit: Int = 100
    ): List<List<Double>>

    @GET("api/v3/ticker/price")
    suspend fun getPrice(@Query("symbol") symbol: String): PriceCrypto

    @GET("api/v3/ticker/24hr")
    suspend fun getPercentageByDay(@Query("symbol") symbol: String): PercentageCrypto
}