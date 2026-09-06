# Architecture Explained Simply

## What "architecture" means

Architecture is the plan for how the parts of the application are separated and how they communicate. Good separation lets students change one part, such as replacing manual entry with Bluetooth data, without rewriting the entire application.

## Complete project flow

```text
BLE sensor OR manual form
            |
            v
Validate and prepare readings
            |
            v
Run the ONNX model on the phone
            |
            v
Normal / Moderate / Critical / Uncertain
            |
            v
Display result and save an encrypted local record
```

No internet or cloud server is required in this flow.

## The five project layers

| Layer | Simple meaning | Main responsibility | Status |
|---|---|---|---|
| 1. Presentation | What the user sees | Form, buttons, result, history | Starter form complete |
| 2. Sensor integration | Where readings come from | BLE connection or manual input | Manual input complete; BLE planned |
| 3. Edge intelligence | The local "brain" | Validate features and run ONNX inference | Interface complete; ONNX planned |
| 4. Data management | Local memory | Encrypt and store assessments | Planned |
| 5. Runtime | Phone environment | Android compatibility, speed, memory and battery | Basic Android target configured |

## Architecture inside the Android code

The starter follows MVVM:

```text
View (Compose screen)
    observes state and sends user actions
                  |
                  v
ViewModel (AssessmentViewModel)
    validates text and coordinates the use case
                  |
                  v
Model / domain code
    SensorReading -> SensorClassifier -> AssessmentResult
```

### View

`RuralEdgeHealthApp.kt` draws the form and result card. It should contain display logic, not machine-learning or database logic.

### ViewModel

`AssessmentViewModel.kt` holds the current form state. It converts valid text into a `SensorReading`, asks a classifier for a result, and gives the result back to the screen.

### Domain model

`SensorReading.kt` and `AssessmentResult.kt` define the data shared between layers. These small data classes prevent each feature from inventing a different representation.

### Classifier interface

`SensorClassifier.kt` is a contract: any classifier must accept a `SensorReading` and return an `AssessmentResult`.

The current `DemoSensorClassifier` uses temporary rules. Later, `OnnxSensorClassifier` will follow the same contract. Because the ViewModel depends on the contract, the UI does not need to change when the real model is added.

## Planned final components

```text
ui/
  assessment/             Form, result and ViewModel
  history/                Saved assessment list

domain/
  model/                  Shared data objects
  classifier/             SensorClassifier contract

data/
  ble/                    BLE scan, connect and read
  local/                  Room database and encrypted storage
  repository/             Coordinates data sources

ml/
  OnnxSensorClassifier    Preprocessing and local inference

app/src/main/assets/
  model.onnx              Trained model
  model_metadata.json     Feature order, units and model version
```

## Rules the team should protect

1. The UI must not call BLE, Room, or ONNX APIs directly.
2. BLE and manual readings must produce the same `SensorReading` format.
3. Core classification must work with airplane mode enabled.
4. Training and Android inference must use the same features, order, units, scaling, labels, and confidence threshold.
5. Sensor records must be encrypted before real or sensitive data is stored.
6. Demo rules and synthetic-data results must never be described as medical validation.

## Security boundary

The final application stores records in Room with SQLCipher encryption. Android Keystore protects the encryption key. Export and future cloud synchronization are optional; neither may be required for local classification.
