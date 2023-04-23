package phonebook

import java.io.File

fun main() {
    val currentDir = System.getProperty("user.dir")
    val contactsFile = File("$currentDir/directory.txt")
    val namesFile = File("$currentDir/find.txt")

    val unsortedContacts: List<Contact> = readContactsFromFile(contactsFile)
    val names: List<String> = namesFile.readLines()
    val contactSearch = ContactSearch()

    println("Start searching (linear search)...")
    val linearSearchResult = contactSearch.linear(unsortedContacts, names)
    println("Found ${linearSearchResult.contacts.size} / ${names.size} entries. Time taken: ${linearSearchResult.elapsedTime()}.")
    println()
    contactSearch.jump(unsortedContacts, names, linearSearchResult.elapsedTimeMillis * 10)
    println()
    contactSearch.binary(unsortedContacts, names, linearSearchResult.elapsedTimeMillis * 10)
    println()
    contactSearch.hash(unsortedContacts, names)
}

fun readContactsFromFile(file: File): List<Contact> {
    return file.readLines().map {
        val (number, name) = it.split(" ", limit = 2)
        Contact(number, name)
    }
}
