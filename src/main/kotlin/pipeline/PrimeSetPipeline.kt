package pipeline

import manager.FileManager
import extractor.PrimeSetExtractor
import logging.Logger
import misc.PrimeSetSyncService
import model.domain.FileSource
import normalizer.PrimeSetNormalizer
import remote.DataSources
import remote.HtmlDownloader

class PrimeSetPipeline(
    private val downloader: HtmlDownloader = HtmlDownloader(),
    private val extractor: PrimeSetExtractor = PrimeSetExtractor(),
    private val normalizer: PrimeSetNormalizer = PrimeSetNormalizer()
) : Pipeline {
    override fun run() {
        Logger.warn("PIPELINE", "===== Prime Sets Pipeline =====")
        val primeDocument = downloader.download(DataSources.PRIME_SETS)
        val rawPrimeSets = extractor.extract(primeDocument)
        val syncResult = PrimeSetSyncService().sync(rawPrimeSets)

        if (syncResult.newPrimeSets.isEmpty()) {
            Logger.info(FileSource.PRIME_SETS.logName, "Prime Sets are already up to date.")
            Logger.warn("PIPELINE", "Pipeline finished successfully.")
            return
        }

        val normalizedPrimeSets = normalizer.normalize(syncResult.newPrimeSets)

        FileManager.exportPrimeSets(syncResult.existing + normalizedPrimeSets)
        Logger.warn("PIPELINE", "Pipeline finished successfully.")
    }
}