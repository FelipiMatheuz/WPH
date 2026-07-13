package pipeline

import logging.Logger
import manager.FileManager

class ManifestPipeline: Pipeline {
    override fun run() {
        Logger.warn("PIPELINE", "===== Manifest Pipeline =====")
        FileManager.exportManifest()
        Logger.warn("PIPELINE", "Pipeline finished successfully.")
    }
}