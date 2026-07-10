package misc

object IdGenerator {
    fun generateId(
        s1: String,
        s2: String? = null
    ): String {

        return if (s2 != null) {
            "${s1}_${s2}"
        } else {
            s1
        }.lowercase()
            .replace(" ", "_")

    }
}
