package com.nasdroid.percentage

import kotlin.math.roundToInt

@JvmInline
value class Percentage private constructor(private val percentage: Float): Comparable<Percentage> {
    override fun compareTo(other: Percentage): Int {
        return percentage.compareTo(other.percentage)
    }

    fun toInt(): Int {
        return (percentage * 100).roundToInt()
    }

    fun toFloat(percentageStyle: PercentageStyle = PercentageStyle.SHORT): Float {
        return when (percentageStyle) {
            PercentageStyle.FULL -> percentage * 100
            PercentageStyle.SHORT -> percentage
        }
    }

    fun toDouble(percentageStyle: PercentageStyle = PercentageStyle.FULL): Double {
        return when (percentageStyle) {
            PercentageStyle.FULL -> percentage.toDouble() * 100
            PercentageStyle.SHORT -> percentage.toDouble()
        }
    }

    companion object {

        val Float.percent: Percentage
            get() = Percentage(this)

        val Double.percent: Percentage
            get() = Percentage(this.toFloat() / 100)

        val Int.percent: Percentage
            get() = Percentage(this / 100f)
    }
}
