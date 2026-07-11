package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError

fun interface ConsistencyRule {

    fun validate(
        context: ConsistencyContext
    ): List<ValidationError>

}