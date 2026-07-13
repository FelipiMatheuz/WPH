package misc

object IdGenerator {
    fun generateId(name: String): String {
        return name.lowercase()
            .trim()
            .replace("&", "and")
            .replace(Regex("[^a-z0-9]+"), "_")
            .removeSurrounding("_")
    }

    fun generateCollectionId(name: String) = "${generateId(name)}_collection"
}
