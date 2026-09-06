package org.ruraledgehealth.app.domain.model

/**
 * A single set of readings accepted by the classifier.
 *
 * BLE input and manual input must both be converted to this same type. The units
 * named here are part of the model contract and must match the training code.
 */
data class SensorReading(
    val heartRateBpm: Int,
    val temperatureCelsius: Double,
    val systolicBp: Int,
    val diastolicBp: Int,
    val spo2Percent: Int,
)
