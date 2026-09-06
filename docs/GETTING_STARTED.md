# Getting Started

This page gets the existing starter application running. No machine-learning knowledge is required for this step.

## 1. Install the required software

- Install Android Studio.
- During installation, include the Android SDK and Android Emulator.
- In Android Studio, install Android SDK Platform 34.
- Use Android Studio's Embedded JDK 17 when asked for a Gradle JDK.

## 2. Open the project

1. Start Android Studio.
2. Select **Open**.
3. Choose the `rural-edge-health` folder, not the `app` folder.
4. Wait for Gradle sync and dependency downloads to finish.
5. Accept any request to install a missing Android SDK component.

## 3. Choose a device

Use either:

- An Android emulator created through **Device Manager**, or
- A physical Android 8.0+ phone with USB debugging enabled.

The long-term target is an entry-level phone with 2 GB RAM. An emulator is enough for learning and early development.

## 4. Run the starter application

1. Select the `app` run configuration.
2. Select the emulator or connected phone.
3. Press **Run**.
4. Enter all five readings.
5. Select **Run offline demo assessment**.

The screen should show Normal, Moderate, or Critical. This result comes from temporary rules, not from the final machine-learning model.

On Windows, tests can also be started from a terminal after JDK 17 is configured:

```powershell
.\gradlew.bat test
```

The first run downloads the project-specific Gradle version.

## 5. Useful sample input

Use these values to test the complete starter flow:

| Field | Test value |
|---|---:|
| Heart rate | 72 |
| Temperature | 36.8 |
| Systolic BP | 120 |
| Diastolic BP | 80 |
| SpO2 | 98 |

The current demo rules should return **Normal**. This is only a software test and has no medical meaning.

## 6. If something fails

- **Gradle JDK error:** select Android Studio's Embedded JDK 17.
- **SDK 34 missing:** install it through **Tools > SDK Manager**.
- **No device found:** start an emulator or enable USB debugging on a phone.
- **Dependency download fails:** check the internet connection and retry Gradle sync.
- **Form shows an error:** make sure every field contains a number within the accepted software-test range.

This repository was initially created in an environment without Java or the Android SDK, so its first full Android Studio build still needs to be performed on a configured development machine.

## 7. What to read next

Read [Architecture](ARCHITECTURE.md), then follow the stages in [Implementation Guide](IMPLEMENTATION_GUIDE.md).
