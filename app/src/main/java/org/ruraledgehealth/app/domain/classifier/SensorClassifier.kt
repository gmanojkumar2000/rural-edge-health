package org.ruraledgehealth.app.domain.classifier

import org.ruraledgehealth.app.domain.model.AssessmentResult
import org.ruraledgehealth.app.domain.model.SensorReading

/**
 * Contract between the app and any classification method.
 *
 * The temporary rule classifier and the future ONNX classifier implement this
 * same function. The ViewModel therefore does not need to know which one is used.
 */
fun interface SensorClassifier {
    fun classify(reading: SensorReading): AssessmentResult
}
