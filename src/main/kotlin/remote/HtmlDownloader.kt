package remote

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HtmlDownloader {
    fun download(url: String): Document {
        println("Downloading Drop Table ${url.substring(url.lastIndexOf("/") + 1)}...")
        return Jsoup
            .connect(url)
            .userAgent("prime-hunt-data")
            .timeout(30_000)
            .get()
    }
}
