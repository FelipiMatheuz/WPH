# Prime Tracker Data

<p>
  <img src="https://img.shields.io/badge/Kotlin-2.2.0-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/JVM-21-ED8B00?logo=openjdk&logoColor=white" alt="JVM">
  <img src="https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle&logoColor=white" alt="Gradle">
  <img src="https://img.shields.io/badge/GitHub_Actions-Automated-2088FF?logo=githubactions&logoColor=white" alt="GitHub Actions">
  <img src="https://img.shields.io/badge/License-GPL--3.0-green" alt="GPL-3.0">
  <img src="https://img.shields.io/badge/Status-Active-brightgreen" alt="Status">
</p>

> **A fully automated ETL pipeline for building and maintaining a reliable Warframe Prime database.**
>
> Prime Tracker Data continuously extracts, normalizes, validates and publishes structured JSON datasets that power the **Prime Tracker** ecosystem.

## 🚀 Overview

Prime Tracker Data is an open-source ETL (Extract, Transform, Load) project that automates the creation and maintenance of a structured Warframe Prime database.

Instead of manually maintaining hundreds of JSON entries, the project continuously gathers information from multiple sources, transforms it into normalized domain models, validates its integrity, and publishes production-ready datasets.

The generated database is completely independent and can be consumed by mobile applications, websites, dashboards or community tools.

## 🎯 Vision

Prime Tracker Data is more than a JSON generator.

Its long-term goal is to become a reliable and continuously maintained data source for the Warframe community, allowing developers to build applications without implementing and maintaining their own scrapers.

The project follows a simple philosophy:

> **Extract once. Validate everything. Publish reliable data.**

## ✨ Features

- 🔄 Fully automated ETL workflow
- 📦 Structured domain models
- ⚙️ Incremental updates
- 🔍 Cross-database consistency validation
- 🛡️ Referential integrity checks
- 📊 Structured execution metrics
- 📝 Structured execution logs
- 🚀 Parallel GitHub Actions execution
- 🧩 Modular pipeline architecture
- 📄 Production-ready JSON database

## 🏗️ Architecture

```
                 Official Sources
                        │
                        ▼
                 Data Extraction
                        │
                        ▼
                  Raw Data Models
                        │
                        ▼
                  Normalization
                        │
                        ▼
                  Domain Models
                        │
                        ▼
                   Validation
                        │
                        ▼
               Incremental Update
                        │
                        ▼
                 JSON Generation
                        │
                        ▼
            Cross-Dataset Validation
                        │
                        ▼
                Published Database
```

Each pipeline is isolated and responsible for a single domain, making the project easier to maintain, extend and test independently.

## 📂 Project Structure

- `src/main/kotlin`:
    - `pipeline/`: Core logic for data processing tasks.
    - `extractor/`: Data extraction from remote sources.
    - `normalizer/`: Name and value normalization logic.
    - `model/`: Data classes and serialization schemas.
    - `consistency/`: Database integrity validation rules.
- `data/`: Generated JSON database files (Relics, Prime Sets, Collections).

## 🔄 ETL Pipelines

### 📋 Relics Pipeline

Responsible for extracting and generating the complete Void Relic database.

Features:

- Official Drop Table parsing
- Reward normalization
- Relic status detection
- Duplicate prevention
- Incremental updates

---

### 📋 Prime Sets Pipeline

Builds the complete Prime catalog.

Features:

- Prime component extraction
- Automatic acquisition detection
- Automatic exclusion of Founder-exclusive and Special items
- Image discovery
- Metadata generation

---

### 📋 Prime Collections Pipeline

Tracks Prime Access history.

Features:

- Current Prime Access detection
- Historical preservation
- Promotional artwork extraction
- Collection generation

---

### 📋 Manifest Pipeline

Generates metadata consumed by client applications.

Includes:

- Database version
- Update timestamp
- File hashes
- Download metadata

---

### ✅ Consistency Pipeline

One of the project's core components.

Instead of validating datasets individually, Prime Tracker Data validates relationships between them.

Current rules include:

- Referential Integrity
- Collection Integrity
- Farmability Validation
- Reachability Validation

This guarantees that every published JSON file is coherent with every other database.

## 🤖 Automation Workflow

The entire ETL is executed automatically through GitHub Actions.

```
                 Setup

                   │

                   ▼

      ┌──────────────────────────┐
      │      Parallel Jobs       │
      ├──────────────────────────┤
      │ Relics                   │
      │ Prime Sets               │
      │ Prime Collections        │
      └──────────────────────────┘

                   │

                   ▼

             Manifest Pipeline

                   │

                   ▼

          Consistency Pipeline

                   │

                   ▼

             Automatic Commit
```

Benefits:

- Parallel execution
- Incremental processing
- Automatic publishing
- Daily scheduled updates
- Manual execution support

## 📈 Observability

Prime Hunt Data includes a lightweight observability layer designed specifically for ETL execution.

Instead of relying solely on `println()` statements, every pipeline emits structured log entries enriched with contextual metadata.

Current capabilities include:

- ISO-8601 timestamps
- Log levels (INFO, WARN, ERROR)
- Structured metadata
- Extraction statistics
- Validation summaries
- Pipeline execution reports

Whenever applicable, log entries also include quantitative metadata such as:

- Number of extracted entities
- Number of generated records
- Validation results
- Pipeline-specific execution information

This makes workflow execution easier to inspect locally while also producing clean logs inside GitHub Actions.

## 🛠️ Technology Stack

| Technology | Purpose |
|------------|---------|
| Kotlin (JVM 21) | Core language |
| Gradle | Build system |
| JSoup | HTML parsing |
| OkHttp | HTTP client |
| Kotlin Serialization | JSON serialization |
| GitHub Actions | CI/CD automation |

## ▶️ Running Locally

Prerequisites:

- JDK 21+
- Gradle

Run an individual pipeline:

```bash
./gradlew relics

./gradlew prime_sets

./gradlew prime_collections

./gradlew manifest

./gradlew consistency
```

## 📊 Generated Data

The generated JSON files are published inside the `data/` directory.

Current datasets include:

- Relics
- Prime Sets
- Prime Collections
- Manifest

All datasets are generated automatically and validated before publication.

## 🧠 Design Principles

The project follows a few core engineering principles:

- Single Responsibility
- Pipeline Isolation
- Incremental Processing
- Domain-Driven Design
- Referential Integrity
- Deterministic Outputs
- Automation over Manual Maintenance
- Source-first Architecture

Whenever possible, business rules are inferred directly from source data instead of manually maintained configuration files.

## 🤝 Contributing

Contributions are always welcome.

If you would like to contribute:

1. Fork this repository.
2. Create a feature branch.
3. Follow the existing architecture and coding style.
4. Open a Pull Request describing your changes.

Bug reports, discussions and architectural suggestions are also appreciated.

# 📜 License

This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)**.

See the [LICENSE](LICENSE) file for more information.

## ⭐ Support the Project

If this project helps you or inspires your own work, consider giving it a **star** on GitHub.

It helps increase the project's visibility and supports future development.

## 📢 Contact

Email: cyberman.studios@gmail.com