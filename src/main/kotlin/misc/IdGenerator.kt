package misc

object IdGenerator {
    fun generateId(name: String): String {
        return name.lowercase()
            .replace("&", "and")
            .replace(" ", "_")
            .replace("", "")
    }

    fun generateCollection(name: String) = "${name.lowercase()}_collection"
}
