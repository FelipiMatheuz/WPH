package pipeline

import logging.Logger
import manager.FileManager

class ManifestPipeline: Pipeline {
    override fun run() {
        Logger.pipelineSection("Manifest")
        FileManager.exportManifest()
        Logger.pipelineSuccess()
    }
}