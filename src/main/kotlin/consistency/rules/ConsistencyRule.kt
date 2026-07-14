package consistency.rules

import consistency.model.ConsistencyContext
import logging.LogMetadata

fun interface ConsistencyRule {

    fun validate(
        context: ConsistencyContext
    ): List<LogMetadata>

}