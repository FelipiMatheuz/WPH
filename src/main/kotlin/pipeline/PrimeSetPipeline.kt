package pipeline

import json.JsonManager
import extractor.PrimeSetExtractor
import misc.IgnoredPrimeSets
import misc.PrimeSetSyncService
import normalizer.PrimeSetNormalizer
import remote.DataSources
import remote.HtmlDownloader

class PrimeSetPipeline(
    private val downloader: HtmlDownloader = HtmlDownloader(),
    private val extractor: PrimeSetExtractor = PrimeSetExtractor(),
    private val normalizer: PrimeSetNormalizer = PrimeSetNormalizer()
) : Pipeline {
    override fun run() {
        val primeDocument = downloader.download(DataSources.PRIME_SETS)
        val rawPrimeSets = extractor.extract(primeDocument)
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

        val normalizedPrimeSets = normalizer.normalize(newPrimeSets)

        JsonManager.exportPrimeSets(sync.existing + normalizedPrimeSets)
    }
}