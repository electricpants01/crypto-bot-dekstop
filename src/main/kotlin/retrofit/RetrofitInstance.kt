package retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    const val BASE_URL = "https://api.binance.com/"

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
            .connectTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .method(original.method, original.body)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //timeout
        .client(getOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val binanceService = retrofit.create(BinanceService::class.java)
}