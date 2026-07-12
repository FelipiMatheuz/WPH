package pipeline

import manager.FileManager
import extractor.PrimeCollectionExtractor
import extractor.PrimeCollectionImageExtractor
import misc.PrimeCollectionSyncService
import normalizer.PrimeCollectionNormalizer
import remote.DataSources
import remote.HtmlDownloader

class PrimeCollectionPipeline(
    private val downloader: HtmlDownloader = HtmlDownloader(),
    private val extractor: PrimeCollectionExtractor = PrimeCollectionExtractor(),
    private val imageExtractor: PrimeCollectionImageExtractor = PrimeCollectionImageExtractor(),
    private val normalizer: PrimeCollectionNormalizer = PrimeCollectionNormalizer()
) : Pipeline {
    override fun run() {
        val collectionDocument = downloader.download(DataSources.PRIME_COLLECTION)
        val currentCollection = extractor.extract(collectionDocument)

        if (PrimeCollectionSyncService().collectionExists(currentCollection)) {
            println("Prime Collections are already up to date.")
            return
        }

        val promoImageDocument = downloader.download(DataSources.PRIME_ACCESS)
        val promoImage = imageExtractor.extract(promoImageDocument)
        currentCollection.promoUrl = promoImage

        val primeCollections = normalizer.normalize(currentCollection)

        FileManager.exportPrimeCollections(primeCollections)
    }
}