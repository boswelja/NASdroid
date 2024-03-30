package com.nasdroid.temperature

import com.ionspin.kotlin.bignum.decimal.BigDecimal

private val CELSIUS_KELVIN_OFFSET = BigDecimal.fromDouble(273.15)
private val FAHRENHEIT_KELVIN_OFFSET = BigDecimal.fromDouble(459.67)
private val FAHRENHEIT_KELVIN_FACTOR = BigDecimal.fromDouble(1.8)
private val RANKINE_KELVIN_FACTOR = BigDecimal.fromDouble(1.8)
private val REAUMUR_KELVIN_OFFSET = BigDecimal.fromDouble(273.15)
private val REAUMUR_KELVIN_FACTOR = BigDecimal.fromDouble(1.25)

/**
 * Defines various units supported by [Temperature]. We can convert to/from any of these.
 */
@Suppress("MagicNumber")
enum class TemperatureUnit(
    internal val toKelvin: (BigDecimal) -> BigDecimal,
    internal val fromKelvin: (BigDecimal) -> BigDecimal,
) {
    KELVIN({ it }, { it }),
    CELSIUS(
        toKelvin = {
            it + CELSIUS_KELVIN_OFFSET
        },
        fromKelvin = {
            it - CELSIUS_KELVIN_OFFSET
        }
    ),
    FAHRENHEIT(
        toKelvin = {
            (it + FAHRENHEIT_KELVIN_OFFSET) / FAHRENHEIT_KELVIN_FACTOR
        },
        fromKelvin = {
            (it * FAHRENHEIT_KELVIN_FACTOR) - FAHRENHEIT_KELVIN_OFFSET
        }
    ),
    RANKINE(
        toKelvin = {
            it / RANKINE_KELVIN_FACTOR
        },
        fromKelvin = {
            it * RANKINE_KELVIN_FACTOR
        }
    ),
    REAUMUR(
        toKelvin = {
            (it * REAUMUR_KELVIN_FACTOR) + REAUMUR_KELVIN_OFFSET
        },
        fromKelvin = {
            (it - REAUMUR_KELVIN_OFFSET) / REAUMUR_KELVIN_FACTOR
        }
    )
}
