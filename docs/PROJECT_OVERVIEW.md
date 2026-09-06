# RuralEdgeHealth - Simple Project Breakdown

## 1. What the project is about

RuralEdgeHealth is an Android application that reads IoT sensor data and classifies it directly on the phone as **Normal**, **Moderate**, or **Critical**.

The main idea is to make the system work in rural or low-resource areas where:

- Internet access may be unavailable or unreliable.
- Only low-cost Android phones may be available.
- Battery power and memory are limited.
- Sending sensitive sensor data to the cloud may be undesirable.

The phone receives readings from Bluetooth Low Energy (BLE) sensors. If a sensor is unavailable, the user can enter the readings manually. A small machine-learning model runs locally, displays the classification, and saves the result securely on the device.

This is a **reference architecture and Phase 1 feasibility project**. It demonstrates that offline sensor classification is possible; it is not a clinically validated medical diagnosis system.

## 2. Project goal

Build a small offline-first Android prototype that can:

1. Collect sensor readings through BLE or manual entry.
2. Validate and prepare the readings.
3. Run a lightweight ML model on the phone without internet access.
4. Show a simple three-level result: Normal, Moderate, or Critical.
5. Store readings and results securely for later viewing or export.
6. Operate acceptably on an Android 8.0+ phone with 2 GB RAM.

## 3. Scope

### In scope

- Android mobile application.
- Offline operation for every core feature.
- BLE sensor discovery, connection, reading, and reconnection.
- Manual input when BLE is unavailable.
- Classification using a Random Forest model converted to ONNX.
- Input validation, preprocessing, prediction, and confidence handling.
- Local history of sensor readings and predictions.
- Encrypted local storage and Android-managed encryption keys.
- Simple, accessible screens for non-technical users.
- Basic performance testing for accuracy, speed, memory, battery use, and app size.

### Outside the initial scope

- Medical diagnosis or use in critical healthcare decisions.
- A system validated with real patients or long-term field data.
- Mandatory cloud services or continuous synchronization.
- iOS support.
- Advanced sensors such as Zigbee or ANT+.
- Federated learning, automatic local retraining, or peer-to-peer emergency relaying.
- Large-scale usability studies.

These can be treated as future improvements after the basic prototype works.

## 4. Architecture requirements

The project uses five logical layers.

### Layer 1 - Presentation and interface

Purpose: Let the user enter data and understand results easily.

Requirements:

- Android UI built with Jetpack Compose.
- MVVM structure for UI state and business logic.
- BLE input and manual input options.
- Clear Normal, Moderate, Critical, and Uncertain results.
- Simple navigation, readable text, icons, and color cues.
- Core screens must work without internet.

### Layer 2 - IoT sensor integration

Purpose: Collect readings from BLE sensors.

Requirements:

- BLE device discovery, pairing, connection, and reconnection.
- Read sensor values through GATT services and characteristics.
- Use standard sensor profiles/UUID mappings where possible.
- Validate incoming readings before processing them.
- Fall back to the same manual-entry pipeline if a sensor fails.
- Disconnect or reduce BLE activity when idle to save battery.

### Layer 3 - Edge intelligence and processing

Purpose: Classify the readings entirely on the phone.

Requirements:

- Random Forest model trained on tabular sensor data.
- Model exported to ONNX and bundled with the app.
- ONNX Runtime Mobile for CPU inference.
- Processing pipeline: validate -> normalize -> calculate derived features -> predict -> interpret confidence.
- Output Normal, Moderate, or Critical; return Uncertain when confidence is too low.
- No cloud call is allowed for the core prediction.

The reference dataset uses 15 inputs, including vital readings, calculated values, device information, and user context. For a college prototype, use only the inputs that are clearly defined and available, but the exact same feature order and preprocessing must be used during both training and Android inference.

### Layer 4 - Data management and persistence

Purpose: Save data securely and make history available offline.

Requirements:

- Room database for local records.
- SQLCipher/AES-256 encryption for stored data.
- Android Keystore for encryption-key management.
- Store raw readings, derived features, prediction, confidence, and timestamp.
- Allow local history viewing and optional export/backup.
- Core operation must not depend on synchronization.

### Layer 5 - Infrastructure and runtime

Purpose: Keep the application usable on low-cost hardware.

Requirements:

- Minimum Android API 26 (Android 8.0).
- Target device: 2 GB RAM and an ARM-based processor.
- Memory-aware loading and caching.
- Energy-aware BLE sampling and background work.
- CPU-based ONNX inference optimized for mobile devices.
- Graceful error handling when a sensor, model, or stored record is unavailable.

## 5. Simple data flow

1. User connects a BLE sensor or enters values manually.
2. The app checks that each value is present and within an allowed range.
3. The app applies the same preprocessing used during model training.
4. The ONNX model runs locally on the phone.
5. The app displays the status and confidence level.
6. The reading and result are encrypted and saved locally.
7. The user can view past results or export them later.

## 6. Suggested implementation pieces

- **Android app:** Kotlin, Jetpack Compose, and MVVM.
- **Sensor module:** Android BLE/GATT APIs.
- **ML training:** Python with scikit-learn and Random Forest.
- **Model conversion:** `sklearn-onnx` using ONNX opset 11 or a compatible version.
- **Mobile inference:** ONNX Runtime Mobile.
- **Local database:** Room with SQLCipher.
- **Security:** Android Keystore and local authentication.

## 7. Minimum success criteria

The prototype is successful if it can demonstrate the full workflow offline on a low-end Android device:

- BLE or manual readings can be entered successfully.
- The on-device model returns a valid classification.
- Results are saved and can be viewed again without internet.
- Sensitive local data is encrypted.
- Inference completes in under 100 ms on the target device.
- Peak active memory stays below 200 MB.
- App package size stays below 50 MB.
- Mixed-use battery consumption targets less than 4% per hour.
- Model accuracy remains above 95% on the chosen test data.

The paper reports 98.25% accuracy, about 87 ms inference time on a Samsung Galaxy A10, and a 3.5 MB model. These are reference benchmarks, not results the college project should claim until they are reproduced through its own testing.

## 8. Important limitations to state in the report

- The reference work mainly uses synthetic data, so real-world accuracy is not proven.
- Real BLE sensors may introduce noise, missing readings, and calibration errors.
- Normal/Moderate/Critical labels demonstrate classification; they do not provide a medical diagnosis.
- The original usability study was small, and non-technical users found the interface more difficult.
- Real deployment would need better accessibility, real sensor data, field testing, and appropriate domain or medical review.
