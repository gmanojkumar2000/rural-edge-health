package org.ruraledgehealth.app.domain.classifier

import org.ruraledgehealth.app.domain.model.AssessmentResult
import org.ruraledgehealth.app.domain.model.SensorReading
import org.ruraledgehealth.app.domain.model.SensorStatus

/**
 * Temporary rules for exercising the offline UI flow.
 * This is not the project ML model and must not be used for medical decisions.
 */
class DemoSensorClassifier : SensorClassifier {
    override fun classify(reading: SensorReading): AssessmentResult {
        // Check the most severe demo conditions first so they are not accidentally
        // returned as Moderate by the broader rules below.
        val critical = reading.spo2Percent < 90 ||
            reading.systolicBp >= 180 ||
            reading.temperatureCelsius >= 40.0 ||
            reading.heartRateBpm < 40 ||
            reading.heartRateBpm > 140

        val moderate = reading.spo2Percent < 95 ||
            reading.systolicBp >= 140 ||
            reading.diastolicBp >= 90 ||
            reading.temperatureCelsius >= 38.0 ||
            reading.heartRateBpm < 50 ||
            reading.heartRateBpm > 110

        // These confidence values are placeholders used to exercise the UI. The
        // ONNX implementation must return probabilities produced by the model.
        return when {
            critical -> AssessmentResult(
                status = SensorStatus.CRITICAL,
                confidence = 0.80f,
                message = "Demo rule triggered. Verify the reading with an appropriate professional.",
            )
            moderate -> AssessmentResult(
                status = SensorStatus.MODERATE,
                confidence = 0.75f,
                message = "Demo rule detected a reading outside its normal test range.",
            )
            else -> AssessmentResult(
                status = SensorStatus.NORMAL,
                confidence = 0.72f,
                message = "No demo rule was triggered. This is not a medical assessment.",
            )
        }
    }
}
