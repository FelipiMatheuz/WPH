package consistency.model

import model.domain.FileSource

data class ValidationError(
    val source: FileSource,
    val message: String
)
