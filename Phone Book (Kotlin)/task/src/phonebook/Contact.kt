package phonebook

data class Contact(val number: String, val name: String) {
    override fun toString(): String {
        return "$name $number"
    }

    operator fun compareTo(other: Contact): Int {
        return name.compareTo(other.name)
    }
}
