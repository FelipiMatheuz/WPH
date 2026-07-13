package pipeline

import manager.FileManager
import extractor.PrimeCollectionExtractor
import extractor.PrimeCollectionImageExtractor
import logging.Logger
import misc.PrimeCollectionSyncService
import model.domain.FileSource
import normalizer.PrimeCollectionNormalizer
import remote.DataSources
import remote.HtmlDownloader

class PrimeCollectionPipeline(
    private val downloader: HtmlDownloader = HtmlDownloader(),
    private val extractor: PrimeCollectionExtractor = PrimeCollectionExtractor(),
    private val imageExtractor: PrimeCollectionImageExtractor = PrimeCollectionImageExtractor(),
    private val syncer: PrimeCollectionSyncService = PrimeCollectionSyncService(),
    private val normalizer: PrimeCollectionNormalizer = PrimeCollectionNormalizer()
) : Pipeline {
    override fun run() {
        Logger.warn("PIPELINE", "===== Prime Collections Pipeline =====")
        val collectionDocument = downloader.download(DataSources.PRIME_COLLECTION)
        val currentCollection = extractor.extract(collectionDocument)

        if (syncer.collectionExists(currentCollection)) {
            Logger.info(
                FileSource.PRIME_COLLECTIONS.logName,
                "Prime Collections are already up to date."
            )
            Logger.warn("PIPELINE", "Pipeline finished successfully.")
            return
        }

        val promoImageDocument = downloader.download(DataSources.PRIME_ACCESS)
        val promoImage = imageExtractor.extract(promoImageDocument)

        val primeCollections = normalizer.normalize(currentCollection, promoImage)

        FileManager.exportPrimeCollections(primeCollections)
        Logger.warn("PIPELINE", "Pipeline finished successfully.")
    }
}