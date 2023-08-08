package data

enum class NetworkingState(val message: String) {
    INITIAL("aun no hay datos"),
    LOADING("Loading..."),
    FINISHED("Done")
}