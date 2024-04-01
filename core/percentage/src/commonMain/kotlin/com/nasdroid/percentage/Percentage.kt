package com.nasdroid.percentage

import kotlin.math.roundToInt

/**
 * Represents a percentage value.
 *
 * To construct a Percentage, use the extension functions [percent].
 *
 * To get the value of this percentage expressed in a particular [PercentageStyle], use [toInt],
 * [toFloat] or [toDouble].
 */
@JvmInline
value class Percentage private constructor(private val percentage: Float): Comparable<Percentage> {
    override fun compareTo(other: Percentage): Int {
        return percentage.compareTo(other.percentage)
    }

    /**
     * Adds [other] to this Percentage. This currently does **not** handle integer overflow.
     */
    operator fun plus(other: Percentage): Percentage {
        return Percentage(percentage + other.percentage)
    }

    /**
     * Subtracts [other] from this Percentage. This currently does **not** handle integer overflow.
     */
    operator fun minus(other: Percentage): Percentage {
        return Percentage(percentage - other.percentage)
    }

    /**
     * Divides this percentage by [other]. This currently does **not** handle integer overflow.
     */
    operator fun div(other: Percentage): Percentage {
        return Percentage(percentage / other.percentage)
    }

    /**
     * Multiplies this percentage by [other]. This currently does **not** handle integer overflow.
     */
    operator fun times(other: Percentage): Percentage {
        return Percentage(percentage * other.percentage)
    }

    /**
     * Converts this percentage to an Integer representation. This will always be
     * [PercentageStyle.FULL] (i.e. 0 == 0%, 100 == 100%), and any decimal values will be rounded to
     * the nearest whole number.
     */
    fun toInt(): Int {
        return (percentage * 100).roundToInt()
    }

    /**
     * Converts this percentage to a Floating-point representation. Can be one of
     * [PercentageStyle.SHORT] (i.e. 0% == 0f, 100% == 1f), or [PercentageStyle.FULL] (i.e. 0% ==
     * 0f, 100% == 100f).
     */
    fun toFloat(percentageStyle: PercentageStyle = PercentageStyle.SHORT): Float {
        return when (percentageStyle) {
            PercentageStyle.FULL -> percentage * 100
            PercentageStyle.SHORT -> percentage
        }
    }

    /**
     * Converts this percentage to a Double representation. Can be one of
     * [PercentageStyle.SHORT] (i.e. 0% == 0.0, 100% == 1.0), or [PercentageStyle.FULL] (i.e. 0% ==
     * 0.0, 100% == 100.0).
     */
    fun toDouble(percentageStyle: PercentageStyle = PercentageStyle.FULL): Double {
        return when (percentageStyle) {
            PercentageStyle.FULL -> percentage.toDouble() * 100
            PercentageStyle.SHORT -> percentage.toDouble()
        }
    }

    companion object {

        /**
         * Converts a Floating-point percentage representation to a [Percentage]. It is assumed that
         * the Float is of [PercentageStyle.SHORT] (i.e. 0% == 0f, 100% == 1f).
         */
        val Float.percent: Percentage
            get() = Percentage(this)

        /**
         * Converts a Double percentage representation to a [Percentage]. It is assumed that the
         * Double is of [PercentageStyle.FULL] (i.e. 0% == 0.0, 100% == 100.0).
         */
        val Double.percent: Percentage
            get() = Percentage(this.toFloat() / 100)

        /**
         * Converts an Integer percentage representation to a [Percentage].
         */
        val Int.percent: Percentage
            get() = Percentage(this / 100f)
    }
}
