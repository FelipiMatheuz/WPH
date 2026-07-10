package pipeline

import exporter.JsonExporter
import extractor.PrimeSetExtractor
import misc.IgnoredPrimeSets
import misc.PrimeSetSyncService
import normalizer.PrimeSetNormalizer
import remote.DataSources
import remote.HtmlDownloader
import validator.PrimeSetValidator

class PrimeSetPipeline : Pipeline {
    override fun run() {
        val primeDocument = HtmlDownloader().download(DataSources.PRIME_SETS)
        val rawPrimeSets = PrimeSetExtractor().extract(primeDocument)
        val sync = PrimeSetSyncService().sync(rawPrimeSets)

        if (sync.newPrimeSets.isEmpty()) {
            println("Prime Sets are already up to date.")
            return
        }

        val newPrimeSets = IgnoredPrimeSets.update(sync.newPrimeSets)

        if (newPrimeSets.isEmpty()){
            println("No new valid prime sets found.")
            return
        }

        val normalizedPrimeSets = PrimeSetNormalizer().normalize(newPrimeSets)

        PrimeSetValidator().validate(normalizedPrimeSets)
        JsonExporter().exportPrimeSets(sync.existing + normalizedPrimeSets)
    }
}