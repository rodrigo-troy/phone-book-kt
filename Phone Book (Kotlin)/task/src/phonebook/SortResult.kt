package phonebook

/**
 * Created with IntelliJ IDEA.
$ Project: Phone Book (Kotlin)
 * User: rodrigotroy
 * Date: 23-04-23
 * Time: 14:19
 */
data class SortResult(val sortedLines: List<Contact>, override val elapsedTimeMillis: Long, val isSorted: Boolean) :
    IResult
