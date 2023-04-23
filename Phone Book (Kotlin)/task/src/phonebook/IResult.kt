package phonebook

/**
 * Created with IntelliJ IDEA.
$ Project: Phone Book (Kotlin)
 * User: rodrigotroy
 * Date: 23-04-23
 * Time: 15:02
 */
interface IResult {
    val elapsedTimeMillis: Long

    fun elapsedTime(): String {
        val seconds = elapsedTimeMillis / 1000
        val minutes = seconds / 60
        val millis = elapsedTimeMillis % 1000
        return "$minutes min. $seconds sec. $millis ms"
    }
}
