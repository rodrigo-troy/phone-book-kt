package phonebook

import kotlin.math.floor
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

/**
 * Created with IntelliJ IDEA.
$ Project: Phone Book (Kotlin)
 * User: rodrigotroy
 * Date: 23-04-23
 * Time: 18:49
 */
class ContactSearch {
    private val sort = ContactSort()

    fun linear(unsortedContacts: List<Contact>, names: List<String>): SearchResult {
        val linearSearchResults: List<Contact?>
        val elapsedTimeMillis = measureTimeMillis {
            val unsortedLines: List<Contact> = unsortedContacts

            linearSearchResults = names.map { name ->
                unsortedLines.firstOrNull { contact -> contact.name == name }
            }
        }

        val searchResult = SearchResult(linearSearchResults.filterNotNull(), elapsedTimeMillis)

        return searchResult
    }

    fun jump(unsortedContacts: List<Contact>, names: List<String>, maxTimeMillis: Long) {
        println("Start searching (bubble sort + jump search)...")

        val sortResult: SortResult = sort.bubbleSort(unsortedContacts, maxTimeMillis)

        if (!sortResult.isSorted) {
            val linearSearchResult = linear(sortResult.sortedLines, names)

            val elapsedTimeMillis = sortResult.elapsedTimeMillis + linearSearchResult.elapsedTimeMillis
            val seconds = elapsedTimeMillis / 1000
            val minutes = seconds / 60
            val millis = elapsedTimeMillis % 1000

            println("Found ${linearSearchResult.contacts.size} / ${names.size} entries. Time taken: $minutes min. $seconds sec. $millis ms.")
            println("Sorting time: ${sortResult.elapsedTime()}. - STOPPED, moved to linear search")
            println("Searching time: ${linearSearchResult.elapsedTime()}.")
            return
        }

        val jumpSearchResults: SearchResult
        val jumpTimeMillis = measureTimeMillis {
            jumpSearchResults = jump(names, sortResult.sortedLines)
        }

        val elapsedTimeMillis = sortResult.elapsedTimeMillis + jumpTimeMillis
        val seconds = elapsedTimeMillis / 1000
        val minutes = seconds / 60
        val millis = elapsedTimeMillis % 1000

        println("Found ${jumpSearchResults.contacts.size} / ${names.size} entries. Time taken: $minutes min. $seconds sec. $millis ms.")
        println("Sorting time: ${sortResult.elapsedTime()}.")
        println("Searching time: ${jumpSearchResults.elapsedTime()}.")

    }

    fun binary(unsortedContacts: List<Contact>, names: List<String>, maxTimeMillis: Long) {
        println("Start searching (quick sort + binary search)...")

        val sortResult: SortResult = sort.quickSort(unsortedContacts, maxTimeMillis)

        if (!sortResult.isSorted) {
            val linearSearchResult = linear(sortResult.sortedLines, names)

            val elapsedTimeMillis = sortResult.elapsedTimeMillis + linearSearchResult.elapsedTimeMillis
            val seconds = elapsedTimeMillis / 1000
            val minutes = seconds / 60
            val millis = elapsedTimeMillis % 1000

            println("Found ${linearSearchResult.contacts.size} / ${names.size} entries. Time taken: $minutes min. $seconds sec. $millis ms.")
            println("Sorting time: ${sortResult.elapsedTime()}. - STOPPED, moved to linear search")
            println("Searching time: ${linearSearchResult.elapsedTime()}.")
            return
        }

        val binarySearchResults: SearchResult
        val binaryTimeMillis = measureTimeMillis {
            binarySearchResults = binary(names, sortResult.sortedLines)
        }

        val elapsedTimeMillis = sortResult.elapsedTimeMillis + binaryTimeMillis
        val seconds = elapsedTimeMillis / 1000
        val minutes = seconds / 60
        val millis = elapsedTimeMillis % 1000

        println("Found ${binarySearchResults.contacts.size} / ${names.size} entries. Time taken: $minutes min. $seconds sec. $millis ms.")
        println("Sorting time: ${sortResult.elapsedTime()}.")
        println("Searching time: ${binarySearchResults.elapsedTime()}.")
    }

    fun hash(unsortedContacts: List<Contact>, names: List<String>) {
        println("Start searching (hash table)...")

        val hashTable: HashMap<String, Contact>
        val hashCreateTimeMillis = measureTimeMillis {
            hashTable = hashCreate(unsortedContacts)
        }

        val createElapsedTimeMillis = hashCreateTimeMillis
        val createSeconds = createElapsedTimeMillis / 1000
        val createMinutes = createSeconds / 60
        val createMillis = createElapsedTimeMillis % 1000

        val hashSearchResults: SearchResult = hash(names, hashTable)

        val totalElapsedTimeMillis = hashCreateTimeMillis + hashSearchResults.elapsedTimeMillis
        val totalSeconds = totalElapsedTimeMillis / 1000
        val totalMinutes = totalSeconds / 60
        val totalMillis = totalElapsedTimeMillis % 1000

        println("Found ${hashSearchResults.contacts.size} / ${names.size} entries. Time taken: $totalMinutes min. $totalSeconds sec. $totalMillis ms.")
        println("Creating time: $createMinutes min. $createSeconds sec. $createMillis ms..")
        println("Searching time: ${hashSearchResults.elapsedTime()}.")
    }

    private fun binary(names: List<String>, contacts: List<Contact>): SearchResult {
        val found = mutableListOf<Contact>()

        val timeMillis = measureTimeMillis {
            names.forEach { name ->
                val index = binarySearchIndex(name, contacts)
                if (index != -1) {
                    found.add(contacts[index])
                }
            }
        }

        return SearchResult(found, timeMillis)
    }

    private fun binarySearchIndex(target: String, contacts: List<Contact>): Int {
        var left = 0
        var right = contacts.size - 1

        while (left <= right) {
            val mid = (left + right) / 2

            when {
                contacts[mid].name == target -> return mid
                contacts[mid].name < target -> left = mid + 1
                else -> right = mid - 1
            }
        }

        return -1
    }

    private fun jump(names: List<String>, contacts: List<Contact>): SearchResult {
        val found = mutableListOf<Contact>()

        val timeMillis = measureTimeMillis {
            names.forEach { name ->
                val index = jumpSearchIndex(name, contacts)
                if (index != -1) {
                    found.add(contacts[index])
                }
            }
        }

        return SearchResult(found, timeMillis)
    }

    private fun jumpSearchIndex(target: String, contacts: List<Contact>): Int {
        if (contacts.isEmpty()) {
            return -1
        }

        val size = contacts.size
        var step = floor(sqrt(size.toDouble())).toInt()
        var prev = 0

        while (contacts[min(step, size) - 1].name < target) {
            prev = step
            step += sqrt(size.toDouble()).toInt()
            if (prev >= size) {
                return -1
            }
        }

        while (contacts[prev].name < target) {
            prev++

            if (prev == min(step, size)) {
                return -1
            }
        }

        return if (contacts[prev].name == target) prev else -1
    }

    private fun hashCreate(unsortedContacts: List<Contact>): HashMap<String, Contact> {
        val hashTable = HashMap<String, Contact>()
        unsortedContacts.forEach { contact ->
            hashTable[contact.name] = contact
        }
        return hashTable
    }

    private fun hash(names: List<String>, hashTable: HashMap<String, Contact>): SearchResult {
        val found = mutableListOf<Contact>()

        val timeMillis = measureTimeMillis {
            names.forEach { name ->
                if (hashTable.containsKey(name)) {
                    found.add(hashTable[name]!!)
                }
            }
        }

        return SearchResult(found, timeMillis)
    }
}
