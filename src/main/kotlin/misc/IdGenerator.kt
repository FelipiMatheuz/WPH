package misc

object IdGenerator {
    fun generateId(name: String): String {
        return name.lowercase()
            .replace("&", "and")
            .replace(Regex("[^a-z0-9]+"), "_")
    }

    fun generateCollection(name: String) = "${name.lowercase()}_collection"
}
