# Prime Hunt Data - ETL

<p align="left">
  <img src="https://img.shields.io/badge/Kotlin-2.2.0-blue?logo=kotlin" alt="Kotlin">
  <img src="https://img.shields.io/badge/Gradle-8.x-blue?logo=gradle" alt="Gradle">
  <img src="https://img.shields.io/badge/License-MIT-green" alt="License">
  <img src="https://img.shields.io/badge/Status-Active-brightgreen" alt="Status">
  <a href="https://github.com/felipimatheuz/prime-hunt-data/actions/workflows/update-data.yml">
    <img src="https://github.com/felipimatheuz/prime-hunt-data/actions/workflows/update-data.yml/badge.svg" alt="Prime Hunt Data Pipeline Status">
  </a>
</p>

## 🚀 Overview
A Kotlin-based automated pipeline for aggregating, normalizing, and validating Warframe data. This repository serves as the data source for the **Prime Tracker** application, maintaining an up-to-date database of Relics, Prime Sets, and Collections.

The project automates the complex task of fetching data from multiple remote sources, transforming it into a structured format, and ensuring its integrity through a series of consistency checks.

### Key Features
- **Automated Extraction**: Scrapes and parses Warframe data using JSoup and OkHttp.
- **Structured Models**: Robust data modeling for Relics, Prime Sets, and Collections.
- **Multi-Stage Pipeline**: Sequential execution to ensure dependencies between data types are handled correctly.
- **Consistency Validation**: Comprehensive checks to prevent broken references or invalid data states.
- **CI/CD Integration**: Fully automated daily updates via GitHub Actions.

## 📁 Project Structure

- `src/main/kotlin`:
    - `pipeline/`: Core logic for data processing tasks.
    - `extractor/`: Data extraction from remote sources.
    - `normalizer/`: Name and value normalization logic.
    - `model/`: Data classes and serialization schemas.
    - `consistency/`: Database integrity validation rules.
- `data/`: Generated JSON database files (Relics, Prime Sets, Collections).
- `config/`: Configuration files for the pipeline execution.

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/) (JVM 21)
- **Networking**: [OkHttp](https://square.github.io/okhttp/)
- **HTML Parsing**: [JSoup](https://jsoup.org/)
- **Serialization**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Build System**: [Gradle](https://gradle.org/)
- **Automation**: GitHub Actions

## 🏃 Getting Started

### Prerequisites
- JDK 21 or higher.

### Local Execution
You can run individual pipeline segments using the provided Gradle tasks:

```bash
# Update Relics data
./gradlew relics

# Update Prime Sets data
./gradlew prime_sets

# Update Prime Collections data
./gradlew prime_collections

# Generate the final manifest
./gradlew manifest

# Run consistency checks
./gradlew consistency
```

## 🤖 Automation Workflow

The pipeline is orchestrated via GitHub Actions (`update-data.yml`) and runs on a daily schedule.

1. **Relics/Sets/Collections**: Independent jobs fetch and process raw data.
2. **Manifest**: Aggregates processed data into a unified manifest.
3. **Consistency**: Validates the entire database for cross-reference integrity.
4. **Commit**: Automatically commits and pushes changes to the repository if the data has evolved.

---
*Maintained by [Cyberman Studios](mailto:cyberman.studios@gmail.com).*


