import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dorkbox.notify.Notify
import okhttp3.internal.notify
import retrofit.CryptoRepository

@Composable
@Preview
fun App() {
    val cryptoRepository = remember { CryptoRepository() }
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
            cryptoRepository.fetchAllCrypto()
            createNotification()
        }) {
            Text(text)
        }
    }
}

fun createNotification() {
    Notify.create()
        .title("Title Text")
        .text("Hello World!")
        .showWarning();
}
fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
