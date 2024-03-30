package com.nasdroid.temperature

import com.nasdroid.temperature.Temperature.Companion.celsius
import com.nasdroid.temperature.Temperature.Companion.fahrenheit
import com.nasdroid.temperature.Temperature.Companion.kelvin
import com.nasdroid.temperature.Temperature.Companion.rankine
import com.nasdroid.temperature.Temperature.Companion.reaumur
import kotlin.test.Test
import kotlin.test.assertEquals

class TemperatureTest {
    data class TestPoint(
        val kelvin: Double,
        val celsius: Double,
        val fahrenheit: Double,
        val rankine: Double,
        val reaumur: Double
    )

    val WATER_FREEZING = TestPoint(
        kelvin = 273.15,
        celsius = 0.0,
        fahrenheit = 32.0,
        rankine = 491.67,
        reaumur = 0.0
    )

    val WATER_BOILING = TestPoint(
        kelvin = 373.15,
        celsius = 100.0,
        fahrenheit = 212.0,
        rankine = 671.67,
        reaumur = 80.0
    )

    private fun testAgainstTestPoint(temperature: Temperature, testPoint: TestPoint) {
        assertEquals(
            testPoint.kelvin,
            temperature.toDouble(TemperatureUnit.KELVIN)
        )
        assertEquals(
            testPoint.celsius,
            temperature.toDouble(TemperatureUnit.CELSIUS)
        )
        assertEquals(
            testPoint.fahrenheit,
            temperature.toDouble(TemperatureUnit.FAHRENHEIT)
        )
        assertEquals(
            testPoint.rankine,
            temperature.toDouble(TemperatureUnit.RANKINE)
        )
        assertEquals(
            testPoint.reaumur,
            temperature.toDouble(TemperatureUnit.REAUMUR)
        )
    }

    @Test
    fun `conversions between water freezing point work`() {
        testAgainstTestPoint(
            WATER_FREEZING.kelvin.kelvin,
            WATER_FREEZING
        )
        testAgainstTestPoint(
            WATER_FREEZING.celsius.celsius,
            WATER_FREEZING
        )
        testAgainstTestPoint(
            WATER_FREEZING.fahrenheit.fahrenheit,
            WATER_FREEZING
        )
        testAgainstTestPoint(
            WATER_FREEZING.rankine.rankine,
            WATER_FREEZING
        )
        testAgainstTestPoint(
            WATER_FREEZING.reaumur.reaumur,
            WATER_FREEZING
        )
    }

    @Test
    fun `conversions between water boiling point work`() {
        testAgainstTestPoint(
            WATER_BOILING.kelvin.kelvin,
            WATER_BOILING
        )
        testAgainstTestPoint(
            WATER_BOILING.celsius.celsius,
            WATER_BOILING
        )
        testAgainstTestPoint(
            WATER_BOILING.fahrenheit.fahrenheit,
            WATER_BOILING
        )
        testAgainstTestPoint(
            WATER_BOILING.rankine.rankine,
            WATER_BOILING
        )
        testAgainstTestPoint(
            WATER_BOILING.reaumur.reaumur,
            WATER_BOILING
        )
    }
}