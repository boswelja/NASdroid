package com.boswelja.capacity

import kotlin.math.roundToLong

/**
 * Represents the amount of digital data something uses.
 *
 * Capacity can represent ±8 exbibytes with byte-level accuracy.
 *
 * To construct a Capacity, use the extension functions [kibibytes], [kilobytes], [mebibytes],
 * [megabytes], and so on.
 *
 * To get the value of this Capacity expressed in a particular CapacityUnit, use the functions
 * [toLong], [toDouble], and so on.
 */
@JvmInline
value class Capacity internal constructor(private val rawValue: Long) : Comparable<Capacity> {

    override fun compareTo(other: Capacity): Int {
        return rawValue.compareTo(other.rawValue)
    }

    /**
     * Adds [other] to this Capacity. This currently does **not** handle integer overflow.
     */
    operator fun plus(other: Capacity): Capacity {
        return Capacity(rawValue + other.rawValue)
    }

    /**
     * Subtracts [other] from this Capacity. This currently does **not** handle integer overflow.
     */
    operator fun minus(other: Capacity): Capacity {
        return Capacity(rawValue - other.rawValue)
    }

    /**
     * Converts this Capacity to the given [CapacityUnit], returning a Double representing the
     * precise value.
     */
    fun toDouble(unit: CapacityUnit): Double {
        return rawValue.toDouble() / unit.byteFactor
    }

    /**
     * Converts this Capacity to the given [CapacityUnit], rounding to the nearest whole number.
     */
    fun toLong(unit: CapacityUnit): Long {
        return toDouble(unit).roundToLong()
    }

    companion object {

        private fun Int.toCapacity(unit: CapacityUnit): Capacity = Capacity(this * unit.byteFactor)
        private fun Long.toCapacity(unit: CapacityUnit): Capacity = Capacity(this * unit.byteFactor)
        private fun Float.toCapacity(unit: CapacityUnit): Capacity = Capacity((this * unit.byteFactor).roundToLong())
        private fun Double.toCapacity(unit: CapacityUnit): Capacity = Capacity((this * unit.byteFactor).roundToLong())

        // region bytes
        /** Converts an [Int] representation of bytes to a [Capacity]. */
        val Int.bytes: Capacity get() = Capacity(toLong())

        /** Converts a [Long] representation of bytes to a [Capacity]. */
        val Long.bytes: Capacity get() = Capacity(this)
        // endregion

        // region binary units
        /** Converts an [Int] representation of kibibytes to a [Capacity]. */
        val Int.kibibytes: Capacity get() = toCapacity(CapacityUnit.KIBIBYTE)

        /** Converts an [Int] representation of mebibytes to a [Capacity]. */
        val Int.mebibytes: Capacity get() = toCapacity(CapacityUnit.MEBIBYTE)

        /** Converts an [Int] representation of gibibytes to a [Capacity]. */
        val Int.gibibytes: Capacity get() = toCapacity(CapacityUnit.GIBIBYTE)

        /** Converts an [Int] representation of tebibytes to a [Capacity]. */
        val Int.tebibytes: Capacity get() = toCapacity(CapacityUnit.TEBIBYTE)

        /** Converts an [Int] representation of pebibytes to a [Capacity]. */
        val Int.pebibytes: Capacity get() = toCapacity(CapacityUnit.PEBIBYTE)

        /** Converts an [Int] representation of exibytes to a [Capacity]. */
        val Int.exbibytes: Capacity get() = toCapacity(CapacityUnit.EXBIBYTE)

        /** Converts a [Long] representation of kibibytes to a [Capacity]. */
        val Long.kibibytes: Capacity get() = toCapacity(CapacityUnit.KIBIBYTE)

        /** Converts a [Long] representation of mebibytes to a [Capacity]. */
        val Long.mebibytes: Capacity get() = toCapacity(CapacityUnit.MEBIBYTE)

        /** Converts a [Long] representation of gibibytes to a [Capacity]. */
        val Long.gibibytes: Capacity get() = toCapacity(CapacityUnit.GIBIBYTE)

        /** Converts a [Long] representation of tebibytes to a [Capacity]. */
        val Long.tebibytes: Capacity get() = toCapacity(CapacityUnit.TEBIBYTE)

        /** Converts a [Long] representation of pebibytes to a [Capacity]. */
        val Long.pebibytes: Capacity get() = toCapacity(CapacityUnit.PEBIBYTE)

        /** Converts a [Long] representation of exibytes to a [Capacity]. */
        val Long.exbibytes: Capacity get() = toCapacity(CapacityUnit.EXBIBYTE)

        /** Converts a [Float] representation of kibibytes to a [Capacity]. */
        val Float.kibibytes: Capacity get() = toCapacity(CapacityUnit.KIBIBYTE)

        /** Converts a [Float] representation of mebibytes to a [Capacity]. */
        val Float.mebibytes: Capacity get() = toCapacity(CapacityUnit.MEBIBYTE)

        /** Converts a [Float] representation of gibibytes to a [Capacity]. */
        val Float.gibibytes: Capacity get() = toCapacity(CapacityUnit.GIBIBYTE)

        /** Converts a [Float] representation of tebibytes to a [Capacity]. */
        val Float.tebibytes: Capacity get() = toCapacity(CapacityUnit.TEBIBYTE)

        /** Converts a [Float] representation of pebibytes to a [Capacity]. */
        val Float.pebibytes: Capacity get() = toCapacity(CapacityUnit.PEBIBYTE)

        /** Converts a [Float] representation of exibytes to a [Capacity]. */
        val Float.exbibytes: Capacity get() = toCapacity(CapacityUnit.EXBIBYTE)

        /** Converts a [Double] representation of kibibytes to a [Capacity]. */
        val Double.kibibytes: Capacity get() = toCapacity(CapacityUnit.KIBIBYTE)

        /** Converts a [Double] representation of mebibytes to a [Capacity]. */
        val Double.mebibytes: Capacity get() = toCapacity(CapacityUnit.MEBIBYTE)

        /** Converts a [Double] representation of gibibytes to a [Capacity]. */
        val Double.gibibytes: Capacity get() = toCapacity(CapacityUnit.GIBIBYTE)

        /** Converts a [Double] representation of tebibytes to a [Capacity]. */
        val Double.tebibytes: Capacity get() = toCapacity(CapacityUnit.TEBIBYTE)

        /** Converts a [Double] representation of pebibytes to a [Capacity]. */
        val Double.pebibytes: Capacity get() = toCapacity(CapacityUnit.PEBIBYTE)

        /** Converts a [Double] representation of exbibytes to a [Capacity]. */
        val Double.exbibytes: Capacity get() = toCapacity(CapacityUnit.EXBIBYTE)
        //endregion

        // region decimal units
        /** Converts an [Int] representation of kilobytes to a [Capacity]. */
        val Int.kilobytes: Capacity get() = toCapacity(CapacityUnit.KILOBYTE)

        /** Converts an [Int] representation of megabytes to a [Capacity]. */
        val Int.megabytes: Capacity get() = toCapacity(CapacityUnit.MEGABYTE)

        /** Converts an [Int] representation of gigabytes to a [Capacity]. */
        val Int.gigabytes: Capacity get() = toCapacity(CapacityUnit.GIGABYTE)

        /** Converts an [Int] representation of terabytes to a [Capacity]. */
        val Int.terabytes: Capacity get() = toCapacity(CapacityUnit.TERABYTE)

        /** Converts an [Int] representation of petabytes to a [Capacity]. */
        val Int.petabytes: Capacity get() = toCapacity(CapacityUnit.PETABYTE)

        /** Converts an [Int] representation of exabytes to a [Capacity]. */
        val Int.exabytes: Capacity get() = toCapacity(CapacityUnit.EXABYTE)

        /** Converts a [Long] representation of kilobytes to a [Capacity]. */
        val Long.kilobytes: Capacity get() = toCapacity(CapacityUnit.KILOBYTE)

        /** Converts a [Long] representation of megabytes to a [Capacity]. */
        val Long.megabytes: Capacity get() = toCapacity(CapacityUnit.MEGABYTE)

        /** Converts a [Long] representation of gigabytes to a [Capacity]. */
        val Long.gigabytes: Capacity get() = toCapacity(CapacityUnit.GIGABYTE)

        /** Converts a [Long] representation of terabytes to a [Capacity]. */
        val Long.terabytes: Capacity get() = toCapacity(CapacityUnit.TERABYTE)

        /** Converts a [Long] representation of petabytes to a [Capacity]. */
        val Long.petabytes: Capacity get() = toCapacity(CapacityUnit.PETABYTE)

        /** Converts a [Long] representation of exabytes to a [Capacity]. */
        val Long.exabytes: Capacity get() = toCapacity(CapacityUnit.EXABYTE)

        /** Converts a [Float] representation of kilobytes to a [Capacity]. */
        val Float.kilobytes: Capacity get() = toCapacity(CapacityUnit.KILOBYTE)

        /** Converts a [Float] representation of megabytes to a [Capacity]. */
        val Float.megabytes: Capacity get() = toCapacity(CapacityUnit.MEGABYTE)

        /** Converts a [Float] representation of gigabytes to a [Capacity]. */
        val Float.gigabytes: Capacity get() = toCapacity(CapacityUnit.GIGABYTE)

        /** Converts a [Float] representation of terabytes to a [Capacity]. */
        val Float.terabytes: Capacity get() = toCapacity(CapacityUnit.TERABYTE)

        /** Converts a [Float] representation of petabytes to a [Capacity]. */
        val Float.petabytes: Capacity get() = toCapacity(CapacityUnit.PETABYTE)

        /** Converts a [Float] representation of exabytes to a [Capacity]. */
        val Float.exabytes: Capacity get() = toCapacity(CapacityUnit.EXABYTE)

        /** Converts a [Double] representation of kilobytes to a [Capacity]. */
        val Double.kilobytes: Capacity get() = toCapacity(CapacityUnit.KILOBYTE)

        /** Converts a [Double] representation of megabytes to a [Capacity]. */
        val Double.megabytes: Capacity get() = toCapacity(CapacityUnit.MEGABYTE)

        /** Converts a [Double] representation of gigabytes to a [Capacity]. */
        val Double.gigabytes: Capacity get() = toCapacity(CapacityUnit.GIGABYTE)

        /** Converts a [Double] representation of terabytes to a [Capacity]. */
        val Double.terabytes: Capacity get() = toCapacity(CapacityUnit.TERABYTE)

        /** Converts a [Double] representation of petabytes to a [Capacity]. */
        val Double.petabytes: Capacity get() = toCapacity(CapacityUnit.PETABYTE)

        /** Converts a [Double] representation of exabytes to a [Capacity]. */
        val Double.exabytes: Capacity get() = toCapacity(CapacityUnit.EXABYTE)
        //endregion
    }
}
