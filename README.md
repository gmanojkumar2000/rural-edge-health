# RuralEdgeHealth

RuralEdgeHealth is a college-level Android project that demonstrates how IoT sensor readings can be classified directly on a low-cost phone without using the internet.

The current application accepts five manually entered readings, validates them, runs a temporary local classifier, and displays a result. Bluetooth sensors, the trained ONNX model, and encrypted history are the next implementation stages.

> Educational prototype only: this application is not a medical device and must not be used for diagnosis or treatment decisions.

## Start here

New contributors should open the [student documentation index](docs/README.md). It gives a short reading order and links to:

- The project idea and scope
- Setup instructions
- The architecture and data flow
- The implementation plan
- A glossary of technical terms

## Current status

- [x] Android project structure
- [x] Kotlin and Jetpack Compose screen
- [x] MVVM state handling
- [x] Manual sensor entry and validation
- [x] Replaceable classifier interface
- [x] Demo classifier and basic unit tests
- [ ] Trained Random Forest ONNX model
- [ ] Bluetooth Low Energy sensor input
- [ ] Encrypted Room database and history screen
- [ ] Performance and usability testing
