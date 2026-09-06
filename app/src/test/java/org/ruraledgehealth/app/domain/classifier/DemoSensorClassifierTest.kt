package org.ruraledgehealth.app.domain.classifier

import org.junit.Assert.assertEquals
import org.junit.Test
import org.ruraledgehealth.app.domain.model.SensorReading
import org.ruraledgehealth.app.domain.model.SensorStatus

/** Small examples showing students how classifier behavior can be tested. */
class DemoSensorClassifierTest {
    private val classifier = DemoSensorClassifier()

    @Test
    fun normalDemoReadingReturnsNormal() {
        val result = classifier.classify(
            SensorReading(
                heartRateBpm = 72,
                temperatureCelsius = 36.8,
                systolicBp = 120,
                diastolicBp = 80,
                spo2Percent = 98,
            ),
        )

        assertEquals(SensorStatus.NORMAL, result.status)
    }

    @Test
    fun lowSpo2DemoReadingReturnsCritical() {
        val result = classifier.classify(
            SensorReading(
                heartRateBpm = 72,
                temperatureCelsius = 36.8,
                systolicBp = 120,
                diastolicBp = 80,
                spo2Percent = 88,
            ),
        )

        assertEquals(SensorStatus.CRITICAL, result.status)
    }
}
