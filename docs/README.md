# Student Documentation

Welcome to RuralEdgeHealth. You do not need to understand Android, IoT, and machine learning all at once. Read the documents in this order:

1. [Project Overview](PROJECT_OVERVIEW.md) - what the project solves, its scope, and its limits.
2. [Getting Started](GETTING_STARTED.md) - how to open and run the starter application.
3. [Architecture](ARCHITECTURE.md) - how the parts fit together.
4. [Implementation Guide](IMPLEMENTATION_GUIDE.md) - the recommended development stages.
5. [Glossary](GLOSSARY.md) - short definitions for unfamiliar terms.

The research papers used as references are stored in [`reference/`](reference/). Students should begin with the Markdown documents because they explain the same idea more directly.

## One-minute explanation

Most smart sensor applications send readings to a cloud server for analysis. That becomes unreliable when internet access is poor. RuralEdgeHealth moves the analysis onto an Android phone. The phone receives a reading through Bluetooth or a form, runs a small model locally, shows a status, and saves the result on the device.

## What is already implemented

- Android and Compose project structure
- A manual entry screen
- Input validation
- MVVM state handling
- A replaceable classifier contract
- A temporary demo classifier
- Basic tests for the demo classifier

## What students will build next

- A training dataset and Random Forest training script
- An exported ONNX model
- On-device ONNX inference
- Encrypted history using Room and SQLCipher
- BLE sensor discovery and reading
- Performance and usability tests

## Important honesty rule

This is a feasibility and learning project. The labels are demonstrations of classification, not medical diagnoses. Never copy accuracy or performance numbers from the paper and claim that this implementation achieved them. Measure and report this application's own results.
