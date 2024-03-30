package com.nasdroid.temperature

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlin.math.roundToLong

/**
 * Represents a temperature value.
 *
 * To construct a Temperature, use the extension functions [celsius], [fahrenheit], [kelvin] and so
 * on.
 *
 * To get the value of this Temperature expressed in a particular TemperatureUnit, use the functions
 * [toLong], [toDouble], and so on.
 */
@JvmInline
value class Temperature internal constructor(private val kelvin: BigDecimal) : Comparable<Temperature> {

    override fun compareTo(other: Temperature): Int {
        return kelvin.compareTo(other.kelvin)
    }

    /**
     * Adds [other] to this Temperature. This currently does **not** handle integer overflow.
     */
    operator fun plus(other: Temperature): Temperature {
        return Temperature(kelvin + other.kelvin)
    }

    /**
     * Subtracts [other] from this Temperature. This currently does **not** handle integer overflow.
     */
    operator fun minus(other: Temperature): Temperature {
        return Temperature(kelvin - other.kelvin)
    }

    /**
     * Converts this Temperature to the given [TemperatureUnit], returning a Double representing the
     * precise value.
     */
    fun toDouble(unit: TemperatureUnit): Double {
        return unit.fromKelvin(kelvin).doubleValue(exactRequired = false)
    }

    /**
     * Converts this Temperature to the given [TemperatureUnit], rounding to the nearest whole number.
     */
    fun toLong(unit: TemperatureUnit): Long {
        return toDouble(unit).roundToLong()
    }

    @Suppress("unused")
    companion object {

        private fun Int.toTemperature(unit: TemperatureUnit): Temperature =
            Temperature(unit.toKelvin(this.toBigDecimal()))
        private fun Long.toTemperature(unit: TemperatureUnit): Temperature =
            Temperature(unit.toKelvin(this.toBigDecimal()))
        private fun Float.toTemperature(unit: TemperatureUnit): Temperature =
            Temperature(unit.toKelvin(this.toBigDecimal()))
        private fun Double.toTemperature(unit: TemperatureUnit): Temperature =
            Temperature(unit.toKelvin(this.toBigDecimal()))

        // region kelvin
        /** Converts an [Int] representation of kelvin to a [Temperature]. */
        val Int.kelvin: Temperature
            get() = toTemperature(TemperatureUnit.KELVIN)

        /** Converts an [Long] representation of kelvin to a [Temperature]. */
        val Long.kelvin: Temperature
            get() = toTemperature(TemperatureUnit.KELVIN)

        /** Converts an [Float] representation of kelvin to a [Temperature]. */
        val Float.kelvin: Temperature
            get() = toTemperature(TemperatureUnit.KELVIN)

        /** Converts an [Double] representation of kelvin to a [Temperature]. */
        val Double.kelvin: Temperature
            get() = toTemperature(TemperatureUnit.KELVIN)
        // endregion

        // region celsius
        /** Converts an [Int] representation of celsius to a [Temperature]. */
        val Int.celsius: Temperature
            get() = toTemperature(TemperatureUnit.CELSIUS)

        /** Converts an [Long] representation of celsius to a [Temperature]. */
        val Long.celsius: Temperature
            get() = toTemperature(TemperatureUnit.CELSIUS)

        /** Converts an [Float] representation of celsius to a [Temperature]. */
        val Float.celsius: Temperature
            get() = toTemperature(TemperatureUnit.CELSIUS)

        /** Converts an [Double] representation of celsius to a [Temperature]. */
        val Double.celsius: Temperature
            get() = toTemperature(TemperatureUnit.CELSIUS)
        // endregion

        // region fahrenheit
        /** Converts an [Int] representation of fahrenheit to a [Temperature]. */
        val Int.fahrenheit: Temperature
            get() = toTemperature(TemperatureUnit.FAHRENHEIT)

        /** Converts an [Long] representation of fahrenheit to a [Temperature]. */
        val Long.fahrenheit: Temperature
            get() = toTemperature(TemperatureUnit.FAHRENHEIT)

        /** Converts an [Float] representation of fahrenheit to a [Temperature]. */
        val Float.fahrenheit: Temperature
            get() = toTemperature(TemperatureUnit.FAHRENHEIT)

        /** Converts an [Double] representation of fahrenheit to a [Temperature]. */
        val Double.fahrenheit: Temperature
            get() = toTemperature(TemperatureUnit.FAHRENHEIT)
        // endregion

        // region rankine
        /** Converts an [Int] representation of rankine to a [Temperature]. */
        val Int.rankine: Temperature
            get() = toTemperature(TemperatureUnit.RANKINE)

        /** Converts an [Long] representation of rankine to a [Temperature]. */
        val Long.rankine: Temperature
            get() = toTemperature(TemperatureUnit.RANKINE)

        /** Converts an [Float] representation of rankine to a [Temperature]. */
        val Float.rankine: Temperature
            get() = toTemperature(TemperatureUnit.RANKINE)

        /** Converts an [Double] representation of rankine to a [Temperature]. */
        val Double.rankine: Temperature
            get() = toTemperature(TemperatureUnit.RANKINE)
        // endregion

        // region réaumur
        /** Converts an [Int] representation of réaumur to a [Temperature]. */
        val Int.reaumur: Temperature
            get() = toTemperature(TemperatureUnit.REAUMUR)

        /** Converts an [Long] representation of réaumur to a [Temperature]. */
        val Long.reaumur: Temperature
            get() = toTemperature(TemperatureUnit.REAUMUR)

        /** Converts an [Float] representation of réaumur to a [Temperature]. */
        val Float.reaumur: Temperature
            get() = toTemperature(TemperatureUnit.REAUMUR)

        /** Converts an [Double] representation of réaumur to a [Temperature]. */
        val Double.reaumur: Temperature
            get() = toTemperature(TemperatureUnit.REAUMUR)
        // endregion
    }
}
