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
        val convertedKelvin = temperature.toDouble(TemperatureUnit.KELVIN)
        assertEquals(
            testPoint.kelvin,
            convertedKelvin,
            "Expected ${testPoint.kelvin} K, but got $convertedKelvin K"
        )
        val convertedCelsius = temperature.toDouble(TemperatureUnit.CELSIUS)
        assertEquals(
            testPoint.celsius,
            convertedCelsius,
            "Expected ${testPoint.celsius} °C, but got $convertedCelsius °C"
        )
        val convertedFahrenheit = temperature.toDouble(TemperatureUnit.FAHRENHEIT)
        assertEquals(
            testPoint.fahrenheit,
            convertedFahrenheit,
            "Expected ${testPoint.fahrenheit} °F, but got $convertedFahrenheit °F"
        )
        val convertedRankine = temperature.toDouble(TemperatureUnit.RANKINE)
        assertEquals(
            testPoint.rankine,
            convertedRankine,
            "Expected ${testPoint.rankine} °R, but got $convertedRankine °R"
        )
        val convertedReaumur = temperature.toDouble(TemperatureUnit.REAUMUR)
        assertEquals(
            testPoint.reaumur,
            convertedReaumur,
            "Expected ${testPoint.reaumur} °Ré, but got $convertedReaumur °Ré"
        )
    }

    @Test
    fun `converting kelvin between water freezing points`() {
        testAgainstTestPoint(
            WATER_FREEZING.kelvin.kelvin,
            WATER_FREEZING
        )
    }

    @Test
    fun `converting celsius between water freezing points`() {
        testAgainstTestPoint(
            WATER_FREEZING.celsius.celsius,
            WATER_FREEZING
        )
    }

    @Test
    fun `converting fahrenheit between water freezing points`() {
        testAgainstTestPoint(
            WATER_FREEZING.fahrenheit.fahrenheit,
            WATER_FREEZING
        )
    }

    @Test
    fun `converting rankine between water freezing points`() {
        testAgainstTestPoint(
            WATER_FREEZING.rankine.rankine,
            WATER_FREEZING
        )
    }

    @Test
    fun `converting reaumur between water freezing points`() {
        testAgainstTestPoint(
            WATER_FREEZING.reaumur.reaumur,
            WATER_FREEZING
        )
    }

    @Test
    fun `converting kelvin between water boiling points`() {
        testAgainstTestPoint(
            WATER_BOILING.kelvin.kelvin,
            WATER_BOILING
        )
    }

    @Test
    fun `converting celsius between water boiling points`() {
        testAgainstTestPoint(
            WATER_BOILING.celsius.celsius,
            WATER_BOILING
        )
    }

    @Test
    fun `converting fahrenheit between water boiling points`() {
        testAgainstTestPoint(
            WATER_BOILING.fahrenheit.fahrenheit,
            WATER_BOILING
        )
    }

    @Test
    fun `converting rankine between water boiling points`() {
        testAgainstTestPoint(
            WATER_BOILING.rankine.rankine,
            WATER_BOILING
        )
    }

    @Test
    fun `converting reaumur between water boiling points`() {
        testAgainstTestPoint(
            WATER_BOILING.reaumur.reaumur,
            WATER_BOILING
        )
    }
}
