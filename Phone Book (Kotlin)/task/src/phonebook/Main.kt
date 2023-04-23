package phonebook

import java.io.File
import kotlin.system.measureTimeMillis


fun main() {
    val currentDir = System.getProperty("user.dir")
    val contactsFile = File("$currentDir/directory.txt")
    val namesFile = File("$currentDir/find.txt")

    println("Start searching...")

    val elapsedTimeMillis = measureTimeMillis {
        val lines = readContactsFromFile(contactsFile)
        val names = namesFile.readLines()
        val linearSearchResults = linearSearch(names, lines)

        println("Found ${linearSearchResults.size} / ${names.size} entries.")
    }

    val seconds = elapsedTimeMillis / 1000
    val minutes = seconds / 60
    val millis = elapsedTimeMillis % 1000

    println("Time taken: $minutes min. $seconds sec. $millis ms.")
}

fun readContactsFromFile(file: File): List<Contact> {
    return file.readLines().map {
        val (number, name) = it.split(" ", limit = 2)
        Contact(number, name)
    }
}

fun linearSearch(names: List<String>, contacts: List<Contact>): List<Contact> {
    return names.mapNotNull { name ->
        contacts.firstOrNull { contact -> contact.name == name }
    }
}
