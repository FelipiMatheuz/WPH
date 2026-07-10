package model.raw

import model.domain.relic.AcquisitionSource

data class RelicSource(
    val relicId: String,
    val source: AcquisitionSource
)
