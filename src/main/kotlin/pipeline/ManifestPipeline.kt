package pipeline

import json.JsonManager

class ManifestPipeline: Pipeline {
    override fun run() {
        JsonManager.exportManifest()
    }
}