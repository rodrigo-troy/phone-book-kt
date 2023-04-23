package phonebook

import java.io.File
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis


fun main() {
    val currentDir = System.getProperty("user.dir")
    val contactsFile = File("$currentDir/directory.txt")
    val namesFile = File("$currentDir/find.txt")

    println("Start searching (linear search)...")
    val linearSearchResult = linearSearch(contactsFile, namesFile)
    println("Found ${linearSearchResult.contacts.size} / ${namesFile.readLines().size} entries. Time taken: ${linearSearchResult.elapsedTime()}.")
    println()
    jumpSearch(contactsFile, namesFile, linearSearchResult.elapsedTimeMillis * 10)
}

private fun linearSearch(contacts: List<Contact>, names: List<String>): SearchResult {
    val linearSearchResults: List<Contact?>
    val elapsedTimeMillis = measureTimeMillis {
        val unsortedLines: List<Contact> = contacts

        linearSearchResults = names.map { name ->
            unsortedLines.firstOrNull { contact -> contact.name == name }
        }
    }

    return SearchResult(linearSearchResults.filterNotNull(), elapsedTimeMillis)
}

private fun linearSearch(contactsFile: File, namesFile: File): SearchResult {
    val unsortedLines: List<Contact>
    val readContactsTimeMillis = measureTimeMillis {
        unsortedLines = readContactsFromFile(contactsFile)
    }

    val names: List<String>
    val readNamesTimeMillis = measureTimeMillis {
        names = namesFile.readLines()
    }

    val (contacts, linearElapsedTimeMillis) = linearSearch(unsortedLines, names)
    val elapsedTimeMillis = readContactsTimeMillis + readNamesTimeMillis + linearElapsedTimeMillis

    return SearchResult(contacts, elapsedTimeMillis)
}

private fun jumpSearch(contactsFile: File, namesFile: File, maxTimeMillis: Long) {
    println("Start searching (bubble sort + jump search)...")

    val unsortedLines: List<Contact>
    val readContactsTimeMillis = measureTimeMillis {
        unsortedLines = readContactsFromFile(contactsFile)
    }

    val sortResult: SortResult
    val sortTimeMillis = measureTimeMillis {
        sortResult = bubbleSort(unsortedLines, maxTimeMillis)
    }

    val names: List<String>
    val readNamesTimeMillis = measureTimeMillis {
        names = namesFile.readLines()
    }

    if (!sortResult.isSorted) {
        val linearSearchResult = linearSearch(sortResult.sortedLines, names)

        val elapsedTimeMillis =
            readContactsTimeMillis + sortTimeMillis + readNamesTimeMillis + linearSearchResult.elapsedTimeMillis
        val seconds = elapsedTimeMillis / 1000
        val minutes = seconds / 60
        val millis = elapsedTimeMillis % 1000

        println("Found ${linearSearchResult.contacts.size} / ${namesFile.readLines().size} entries. Time taken: $minutes min. $seconds sec. $millis ms.")
        println("Sorting time: ${sortResult.elapsedTime()}")
        println("Searching time: ${linearSearchResult.elapsedTime()}")
        return
    }


    val jumpSearchResults: List<Contact>
    val jumpTimeMillis = measureTimeMillis {
        jumpSearchResults = jumpSearch(names, sortResult.sortedLines)
    }

    val elapsedTimeMillis = readContactsTimeMillis + sortTimeMillis + readNamesTimeMillis + jumpTimeMillis
    val seconds = elapsedTimeMillis / 1000
    val minutes = seconds / 60
    val millis = elapsedTimeMillis % 1000

    println("Found ${jumpSearchResults.size} / ${names.size} entries. Time taken: $minutes min. $seconds sec. $millis ms.")
}

fun readContactsFromFile(file: File): List<Contact> {
    return file.readLines().map {
        val (number, name) = it.split(" ", limit = 2)
        Contact(number, name)
    }
}

fun bubbleSort(list: List<Contact>, maxTimeMillis: Long): SortResult {
    val sortedList = list.toMutableList()
    var swapped: Boolean

    var currentTimeMillis = 0L

    for (i in sortedList.indices) {
        val sortTimeMillis = measureTimeMillis {
            swapped = false
            for (j in 0 until (sortedList.size - 1 - i)) {
                if (sortedList[j] > sortedList[j + 1]) {
                    val temp = sortedList[j]
                    sortedList[j] = sortedList[j + 1]
                    sortedList[j + 1] = temp
                    swapped = true
                }
            }
        }

        currentTimeMillis += sortTimeMillis

        if (!swapped) break

        if (currentTimeMillis > maxTimeMillis) {
            return SortResult(sortedList, currentTimeMillis, false)
        }
    }

    return SortResult(sortedList, currentTimeMillis, true)
}

fun jumpSearch(names: List<String>, contacts: List<Contact>): List<Contact> {
    val found = mutableListOf<Contact>()

    names.forEach { name ->
        val index = jumpSearchIndex(name, contacts)
        if (index != -1) {
            found.add(contacts[index])
        }
    }

    return found
}

fun jumpSearchIndex(target: String, contacts: List<Contact>): Int {
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

