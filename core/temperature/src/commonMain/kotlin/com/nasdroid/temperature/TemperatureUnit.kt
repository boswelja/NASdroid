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
            (it + 459.67) * (5/9)
        },
        fromKelvin = {
            (it * (9/5)) - 459.67
        }
    ),
    RANKINE(
        toKelvin = {
            it * (5/9)
        },
        fromKelvin = {
            it * (9/5)
        }
    ),
    REAUMUR(
        toKelvin = {
            (it * (5/4)) + 273.15
        },
        fromKelvin = {
            (it - 273.15) * (4/5)
        }
    )
}
