package consistency.model

enum class ValidationSource(val logName: String) {
    PRIME_SETS("Prime Sets"),
    PRIME_COLLECTIONS("Prime Collections"),
    RELICS("Relics"),
    MANIFEST("Manifest")
}