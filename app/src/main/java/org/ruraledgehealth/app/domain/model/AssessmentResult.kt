package org.ruraledgehealth.app.domain.model

/** Categories that the UI knows how to display. */
enum class SensorStatus {
    NORMAL,
    MODERATE,
    CRITICAL,
    UNCERTAIN,
}

/**
 * A classifier's output.
 *
 * Confidence uses the range 0.0 to 1.0. The message explains the result in the
 * interface, but it must never be presented as medical advice.
 */
data class AssessmentResult(
    val status: SensorStatus,
    val confidence: Float,
    val message: String,
)
