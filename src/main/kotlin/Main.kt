import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.NetworkingState
import data.Notificacion
import dorkbox.notify.Notify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import model.Crypto
import retrofit.CryptoRepository
import ui.CryptoUI
import java.awt.TrayIcon
import java.time.Duration

@Composable
@Preview
fun App() {
    val cryptoRepository = remember { CryptoRepository() }
    val currentCryptoList = remember { mutableStateListOf<Crypto>() }
    val progressText = remember { mutableStateOf("") }
    val notification = remember { Notificacion() }
    val updateUI = remember(currentCryptoList.size) {
        when {
            currentCryptoList.size == 0 -> {
                progressText.value = NetworkingState.INITIAL.message
            }

            coinList.size == currentCryptoList.size -> {
                progressText.value = NetworkingState.FINISHED.message
                createNotification(currentCryptoList)
            }

            else -> {
                progressText.value = NetworkingState.LOADING.message
            }
        }
    }

    MaterialTheme {
        CryptoUI(progressText = progressText.value) {
            CoroutineScope(Dispatchers.IO).launch {
                while (true) {
                    progressText.value = NetworkingState.LOADING.message
                    cryptoRepository.fetchAllCrypto(currentCryptoList)
                    delay(Duration.ofMinutes(3).toMillis())
                }
            }
        }
    }
}

fun createNotification(currentCryptoList: SnapshotStateList<Crypto>) {
    val todayDateTime =
        java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    val temp = currentCryptoList.getLongOrShortList()
    val possibleLong = currentCryptoList.getLongCrypto().size
    val possibleShort = currentCryptoList.getShortCrypto().size
    val text = "Possible Long: ${possibleLong}, Possible Short: ${possibleShort}"
    println("$todayDateTime: $text")
    if (temp.size >= 20) {
        Notify.create()
            .title("Date ${todayDateTime}")
            .text(text)
            .show()
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
