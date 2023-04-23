package phonebook

import java.io.File


fun main() {
    val initTime = System.currentTimeMillis()

    println("Start searching...")

    val currentDir = System.getProperty("user.dir")
    val file = File("$currentDir/directory.txt")

    val lines: List<Contact> = file.readLines().map {
        val (number, name) = it.split(" ", limit = 2)
        Contact(number, name)
    }

    val names = File("$currentDir/find.txt").readLines()
    val linearSearch = linearSearch(names, lines)
    val elapsedTimeMillis = System.currentTimeMillis() - initTime
    val seconds = elapsedTimeMillis / 1000
    val minutes = seconds / 60
    val millis = elapsedTimeMillis % 1000

    println("Found ${linearSearch.size} / ${names.size} entries. Time taken: $minutes min. $seconds sec. $millis ms.")
}

fun linearSearch(lines: List<String>, contacts: List<Contact>): List<String> {
    val found = mutableListOf<String>()

    lines.forEach { name ->
        for (contact in contacts) {
            if (contact.name == name) {
                found.add(contact.toString())
                break
            }
        }
    }

    return found
}
