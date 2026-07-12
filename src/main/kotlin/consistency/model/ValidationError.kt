package consistency.model

data class ValidationError(
    val source: ValidationSource,
    val message: String
)
