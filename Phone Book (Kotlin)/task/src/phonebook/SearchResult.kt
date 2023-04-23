package phonebook

/**
 * Created with IntelliJ IDEA.
$ Project: Phone Book (Kotlin)
 * User: rodrigotroy
 * Date: 23-04-23
 * Time: 14:42
 */
data class SearchResult(val contacts: List<Contact>, override val elapsedTimeMillis: Long) : IResult
