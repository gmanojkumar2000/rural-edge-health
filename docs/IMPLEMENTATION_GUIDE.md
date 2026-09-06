# Implementation Guide

Build the project in small stages. Finish and test one stage before starting the next.

## Stage 0 - Run the starter

Goal: confirm that every team member can build and run the existing application.

Steps:

1. Follow [Getting Started](GETTING_STARTED.md).
2. Enter the sample values and run the demo assessment.
3. Run the unit tests from Android Studio.
4. Record setup problems so the instructions can be improved.

Done when: the app launches, the form accepts input, a result appears, and the tests pass.

## Stage 1 - Agree on the model input contract

Goal: define exactly what data the model will receive.

Start with the five primary inputs already represented by `SensorReading`:

| Feature | Type | Unit |
|---|---|---|
| Heart rate | Whole number | beats per minute |
| Temperature | Decimal number | degrees Celsius |
| Systolic blood pressure | Whole number | mmHg |
| Diastolic blood pressure | Whole number | mmHg |
| SpO2 | Whole number | percent |

Decide and document:

- Valid software input range for each feature
- How missing readings are handled
- Any derived features and their formulas
- Feature order sent to the model
- Output-label order
- Confidence threshold for Uncertain

Done when: one versioned metadata file describes the complete contract and both the Python and Android teams agree on it.

## Stage 2 - Train the Random Forest model

Goal: produce a tested model outside the Android application.

Recommended Python workflow:

1. Load the versioned dataset.
2. Check missing values, class counts, and invalid rows.
3. Split data into training and test sets before balancing.
4. Fit preprocessing only on training data.
5. Apply class balancing only to training data when needed.
6. Train a Random Forest classifier.
7. Report accuracy, weighted F1, and per-class results on untouched test data.
8. Save the preprocessing details and label mapping.

Avoid data leakage: test rows must not influence training, balancing, or preprocessing parameters.

Done when: the team can rerun one script and reproduce the saved model and evaluation report.

## Stage 3 - Export and verify ONNX

Goal: create a model that Android can run.

Steps:

1. Convert the trained model using `sklearn-onnx`.
2. Run the same test samples through scikit-learn and ONNX Runtime in Python.
3. Confirm that both implementations return matching labels.
4. Copy the verified model to `app/src/main/assets/model.onnx`.
5. Copy the model contract to `app/src/main/assets/model_metadata.json`.

Done when: ONNX predictions match the original model on the agreed verification samples.

## Stage 4 - Replace the demo classifier

Goal: use the ONNX model inside Android without changing the screen.

Steps:

1. Add ONNX Runtime Mobile to `app/build.gradle.kts`.
2. Create `OnnxSensorClassifier` implementing `SensorClassifier`.
3. Load the model once, not for every button press.
4. Convert `SensorReading` into the exact ordered model input.
5. Run inference on a background dispatcher.
6. Map model output to `SensorStatus`.
7. Return Uncertain when confidence is below the agreed threshold.
8. Replace the ViewModel's `DemoSensorClassifier` dependency.

Done when: the app produces the same result as the Python verification set while the phone is offline.

## Stage 5 - Add encrypted local history

Goal: save each assessment securely on the phone.

Steps:

1. Add Room and SQLCipher dependencies.
2. Define an assessment entity containing inputs, output, confidence, model version, and timestamp.
3. Add a DAO for insert and history queries.
4. Keep database code behind an `AssessmentRepository`.
5. Protect database key material with Android Keystore.
6. Create a simple history screen.

Done when: saved results remain after the app restarts and cannot be read as plain text from the database file.

Do not store real personal or patient-identifying information during student development.

## Stage 6 - Add BLE input

Goal: fill the same model input from a BLE sensor.

Steps:

1. Start with one known sensor and document its GATT service and characteristic UUIDs.
2. Request Bluetooth permissions at runtime.
3. Scan only when the user asks, to reduce battery use.
4. Connect, discover the required service, and subscribe to or read its characteristic.
5. Parse the raw bytes and convert them into the units used by `SensorReading`.
6. Show clear disconnected, scanning, connected, reading, and error states.
7. Keep manual input available when BLE fails.

Done when: BLE and manual entry create the same domain object and use the same classifier path.

## Stage 7 - Test and measure

Test at least:

- Valid, missing, and out-of-range input
- Normal, Moderate, Critical, and Uncertain output
- Airplane-mode operation with Bluetooth enabled
- App restart and history recovery
- BLE disconnect and manual fallback
- Entry-level device inference time and peak memory
- Battery use during a documented workload
- Usability with technical and non-technical students

Keep measured results separate from targets. A target is what the team hopes to achieve; a result is what a repeatable test actually measured.

## Suggested team split

- **Android/UI:** Compose screens and accessibility
- **Machine learning:** dataset, training, evaluation, and ONNX export
- **IoT:** BLE connection and sensor parsing
- **Data/security:** Room, SQLCipher, Keystore, and history
- **Testing/documentation:** test cases, measurements, diagrams, and report

Everyone should still understand the complete data flow and review changes across team boundaries.
