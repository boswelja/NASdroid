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

        private fun Int.toCapacity(unit: CapacityUnit): Capacity {
            return Capacity(this * unit.byteFactor)
        }

        private fun Long.toCapacity(unit: CapacityUnit): Capacity {
            return Capacity(this * unit.byteFactor)
        }

        private fun Float.toCapacity(unit: CapacityUnit): Capacity {
            return Capacity((this * unit.byteFactor).roundToLong())
        }

        private fun Double.toCapacity(unit: CapacityUnit): Capacity {
            return Capacity((this * unit.byteFactor).roundToLong())
        }

        // region bytes
        val Int.bytes: Capacity get() = Capacity(toLong())

        val Long.bytes: Capacity get() = Capacity(this)
        // endregion

        // region binary units
        val Int.kibibytes: Capacity get() = toCapacity(CapacityUnit.KIBIBYTE)

        val Int.mebibytes: Capacity get() = toCapacity(CapacityUnit.MEBIBYTE)

        val Int.gibibytes: Capacity get() = toCapacity(CapacityUnit.GIBIBYTE)

        val Int.tebibytes: Capacity get() = toCapacity(CapacityUnit.TEBIBYTE)

        val Int.pebibytes: Capacity get() = toCapacity(CapacityUnit.PEBIBYTE)

        val Int.exbibytes: Capacity get() = toCapacity(CapacityUnit.EXBIBYTE)

        val Long.kibibytes: Capacity get() = toCapacity(CapacityUnit.KIBIBYTE)

        val Long.mebibytes: Capacity get() = toCapacity(CapacityUnit.MEBIBYTE)

        val Long.gibibytes: Capacity get() = toCapacity(CapacityUnit.GIBIBYTE)

        val Long.tebibytes: Capacity get() = toCapacity(CapacityUnit.TEBIBYTE)

        val Long.pebibytes: Capacity get() = toCapacity(CapacityUnit.PEBIBYTE)

        val Long.exbibytes: Capacity get() = toCapacity(CapacityUnit.EXBIBYTE)

        val Float.kibibytes: Capacity get() = toCapacity(CapacityUnit.KIBIBYTE)

        val Float.mebibytes: Capacity get() = toCapacity(CapacityUnit.MEBIBYTE)

        val Float.gibibytes: Capacity get() = toCapacity(CapacityUnit.GIBIBYTE)

        val Float.tebibytes: Capacity get() = toCapacity(CapacityUnit.TEBIBYTE)

        val Float.pebibytes: Capacity get() = toCapacity(CapacityUnit.PEBIBYTE)

        val Float.exbibytes: Capacity get() = toCapacity(CapacityUnit.EXBIBYTE)

        val Double.kibibytes: Capacity get() = toCapacity(CapacityUnit.KIBIBYTE)

        val Double.mebibytes: Capacity get() = toCapacity(CapacityUnit.MEBIBYTE)

        val Double.gibibytes: Capacity get() = toCapacity(CapacityUnit.GIBIBYTE)

        val Double.tebibytes: Capacity get() = toCapacity(CapacityUnit.TEBIBYTE)

        val Double.pebibytes: Capacity get() = toCapacity(CapacityUnit.PEBIBYTE)

        val Double.exbibytes: Capacity get() = toCapacity(CapacityUnit.EXBIBYTE)
        //endregion

        // region decimal units
        val Int.kilobytes: Capacity get() = toCapacity(CapacityUnit.KILOBYTE)

        val Int.megabytes: Capacity get() = toCapacity(CapacityUnit.MEGABYTE)

        val Int.gigabytes: Capacity get() = toCapacity(CapacityUnit.GIGABYTE)

        val Int.terabytes: Capacity get() = toCapacity(CapacityUnit.TERABYTE)

        val Int.petabytes: Capacity get() = toCapacity(CapacityUnit.PETABYTE)

        val Int.exabytes: Capacity get() = toCapacity(CapacityUnit.EXABYTE)

        val Long.kilobytes: Capacity get() = toCapacity(CapacityUnit.KILOBYTE)

        val Long.megabytes: Capacity get() = toCapacity(CapacityUnit.MEGABYTE)

        val Long.gigabytes: Capacity get() = toCapacity(CapacityUnit.GIGABYTE)

        val Long.terabytes: Capacity get() = toCapacity(CapacityUnit.TERABYTE)

        val Long.petabytes: Capacity get() = toCapacity(CapacityUnit.PETABYTE)

        val Long.exabytes: Capacity get() = toCapacity(CapacityUnit.EXABYTE)

        val Float.kilobytes: Capacity get() = toCapacity(CapacityUnit.KILOBYTE)

        val Float.megabytes: Capacity get() = toCapacity(CapacityUnit.MEGABYTE)

        val Float.gigabytes: Capacity get() = toCapacity(CapacityUnit.GIGABYTE)

        val Float.terabytes: Capacity get() = toCapacity(CapacityUnit.TERABYTE)

        val Float.petabytes: Capacity get() = toCapacity(CapacityUnit.PETABYTE)

        val Float.exabytes: Capacity get() = toCapacity(CapacityUnit.EXABYTE)

        val Double.kilobytes: Capacity get() = toCapacity(CapacityUnit.KILOBYTE)

        val Double.megabytes: Capacity get() = toCapacity(CapacityUnit.MEGABYTE)

        val Double.gigabytes: Capacity get() = toCapacity(CapacityUnit.GIGABYTE)

        val Double.terabytes: Capacity get() = toCapacity(CapacityUnit.TERABYTE)

        val Double.petabytes: Capacity get() = toCapacity(CapacityUnit.PETABYTE)

        val Double.exabytes: Capacity get() = toCapacity(CapacityUnit.EXABYTE)
        //endregion
    }
}
