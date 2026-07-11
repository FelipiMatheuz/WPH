package consistency.model

data class ValidationError(
    val source: String,
    val message: String
)
