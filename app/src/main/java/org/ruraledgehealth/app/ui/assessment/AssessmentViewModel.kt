package org.ruraledgehealth.app.ui.assessment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.ruraledgehealth.app.domain.classifier.DemoSensorClassifier
import org.ruraledgehealth.app.domain.classifier.SensorClassifier
import org.ruraledgehealth.app.domain.model.AssessmentResult
import org.ruraledgehealth.app.domain.model.SensorReading

/** Everything the assessment screen needs to draw its current state. */
data class AssessmentUiState(
    val heartRate: String = "",
    val temperature: String = "",
    val systolicBp: String = "",
    val diastolicBp: String = "",
    val spo2: String = "",
    val result: AssessmentResult? = null,
    val errorMessage: String? = null,
)

/**
 * Coordinates the assessment screen without drawing Android UI elements.
 *
 * The classifier is passed in through the constructor. This simple form of
 * dependency injection makes it possible to replace the demo with ONNX or a fake
 * classifier in tests.
 */
class AssessmentViewModel(
    private val classifier: SensorClassifier = DemoSensorClassifier(),
) : ViewModel() {
    var uiState by mutableStateOf(AssessmentUiState())
        private set

    fun updateHeartRate(value: String) = update { copy(heartRate = value) }

    fun updateTemperature(value: String) = update { copy(temperature = value) }

    fun updateSystolicBp(value: String) = update { copy(systolicBp = value) }

    fun updateDiastolicBp(value: String) = update { copy(diastolicBp = value) }

    fun updateSpo2(value: String) = update { copy(spo2 = value) }

    /** Validates the form, runs local classification, and publishes the result. */
    fun assess() {
        val reading = parseReading()
        if (reading == null) {
            uiState = uiState.copy(
                result = null,
                errorMessage = "Enter valid values in all five fields.",
            )
            return
        }

        uiState = uiState.copy(
            result = classifier.classify(reading),
            errorMessage = null,
        )
    }

    /**
     * Converts screen text to the strongly typed object used by the domain layer.
     * The ranges reject obvious typing or sensor errors; they are software guardrails,
     * not definitions of healthy values.
     */
    private fun parseReading(): SensorReading? {
        val heartRate = uiState.heartRate.toIntOrNull()?.takeIf { it in 20..250 }
        val temperature = uiState.temperature.toDoubleOrNull()?.takeIf { it in 25.0..45.0 }
        val systolic = uiState.systolicBp.toIntOrNull()?.takeIf { it in 50..260 }
        val diastolic = uiState.diastolicBp.toIntOrNull()?.takeIf { it in 30..180 }
        val spo2 = uiState.spo2.toIntOrNull()?.takeIf { it in 50..100 }

        return SensorReading(
            heartRateBpm = heartRate ?: return null,
            temperatureCelsius = temperature ?: return null,
            systolicBp = systolic ?: return null,
            diastolicBp = diastolic ?: return null,
            spo2Percent = spo2 ?: return null,
        )
    }

    private fun update(transform: AssessmentUiState.() -> AssessmentUiState) {
        // A changed input makes the previous result stale, so clear it immediately.
        uiState = uiState.transform().copy(result = null, errorMessage = null)
    }
}
