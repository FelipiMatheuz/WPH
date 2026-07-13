package model.raw

import model.domain.relic.AcquisitionSource

data class RawRelicSource(
    val relicId: String,
    val source: AcquisitionSource
)
