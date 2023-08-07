import model.Crypto


/**
 * this are the column we get
 * 'timestamp', 'open', 'high', 'low', 'close', 'volume',
 * 'close_time', 'quote_asset_volume', 'number_of_trades',
 *  'taker_buy_base_asset_volume', 'taker_buy_quote_asset_volume', 'ignore']
 *  and we need close column
 */
fun List<List<Double>>.getCloseColumn(): List<Double> {
    val ans = mutableListOf<Double>()
    this.forEach {
        ans.add(it[4])
    }
    return ans
}

fun List<Crypto>.getLongOrShortList(): List<Crypto> {
    return this.filter { it.rsi!! <= 35 || it.rsi >= 65 }
}

fun List<Crypto>.getLongCrypto(): List<Crypto> {
    return this.filter { it.rsi!! <= 35 }
}

fun List<Crypto>.getShortCrypto(): List<Crypto> {
    return this.filter { it.rsi!! >= 65 }
}