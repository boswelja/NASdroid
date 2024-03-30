package com.nasdroid.temperature

/**
 * Defines various units supported by [Temperature]. We can convert to/from any of these.
 */
@Suppress("MagicNumber")
enum class TemperatureUnit(
    internal val toKelvin: (Double) -> Double,
    internal val fromKelvin: (Double) -> Double,
) {
    KELVIN({ it }, { it }),
    CELSIUS(
        toKelvin = {
            it + 273.15
        },
        fromKelvin = {
            it - 273.15
        }
    ),
    FAHRENHEIT(
        toKelvin = {
            (it + 459.67) * (5/9.0)
        },
        fromKelvin = {
            (it * 1.8) - 459.67
        }
    ),
    RANKINE(
        toKelvin = {
            it * (5/9.0)
        },
        fromKelvin = {
            it * 1.8
        }
    ),
    REAUMUR(
        toKelvin = {
            (it * 1.25) + 273.15
        },
        fromKelvin = {
            (it - 273.15) * 0.8
        }
    )
}
