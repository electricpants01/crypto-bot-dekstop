package retrofit

import androidx.compose.runtime.snapshots.SnapshotStateList
import coinList
import getCloseColumn
import kotlinx.coroutines.*
import model.Crypto

class CryptoRepository(
    private val binanceService: BinanceService = RetrofitInstance.binanceService
) {

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    var isActive = false


    fun fetchAllCrypto(currentCryptoList: SnapshotStateList<Crypto>) {
        if(isActive) return
        currentCryptoList.clear()
        val tempList = mutableListOf<Deferred<Crypto>>()
        coroutineScope.launch {
            this@CryptoRepository.isActive = true
            for (coin in coinList) {
                val price = async { binanceService.getPrice(coin) }
                val percentage = async { binanceService.getPercentageByDay(coin) }
                val rsi = async { calculateRSI(binanceService.getRsiCrypto(coin).getCloseColumn()) }
                tempList.add( async { Crypto(coin, price.await().price, percentage.await().priceChangePercent, rsi.await()) })
            }
            this@CryptoRepository.isActive = false
            currentCryptoList.addAll(tempList.awaitAll())
        }
    }

    fun calculateRSI(prices: List<Double>, period: Int = 14): Double {
        require(period > 0 && prices.size >= period) { "Invalid period or price data" }
        var gainSum = 0.0
        var lossSum = 0.0
        // Calculate initial gain and loss
        for (i in 1 until period) {
            val priceDiff = prices[i] - prices[i - 1]
            if (priceDiff >= 0) {
                gainSum += priceDiff
            } else {
                lossSum += -priceDiff
            }
        }
        // Calculate average gain and loss
        gainSum /= period
        lossSum /= period

        for (i in period until prices.size) {
            val priceDiff = prices[i] - prices[i - 1]
            if (priceDiff >= 0) {
                gainSum = (gainSum * (period - 1) + priceDiff) / period
                lossSum = (lossSum * (period - 1)) / period
            } else {
                gainSum = (gainSum * (period - 1)) / period
                lossSum = (lossSum * (period - 1) - priceDiff) / period
            }
        }
        // Calculate relative strength
        val relativeStrength = gainSum / lossSum
        // Calculate RSI
        val rsi = 100.0 - (100.0 / (1.0 + relativeStrength))
        return rsi
    }
}