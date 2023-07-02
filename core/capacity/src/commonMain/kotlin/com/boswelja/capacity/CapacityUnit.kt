package com.boswelja.capacity

/**
 * Defines various units supported by [Capacity]. We can convert to/from any of these.
 */
@Suppress("MagicNumber")
enum class CapacityUnit(internal val byteFactor: Long) {
    BYTE(1),

    // Binary units
    KIBIBYTE(1024),
    MEBIBYTE(1048576),
    GIBIBYTE(1073741824),
    TEBIBYTE(1_099_511_627_776),
    PEBIBYTE(1_125_899_906_842_624),
    EXBIBYTE(1_152_921_504_606_846_976),

    // Decimal units
    KILOBYTE(1_000),
    MEGABYTE(1_000_000),
    GIGABYTE(1_000_000_000),
    TERABYTE(1_000_000_000_000),
    PETABYTE(1_000_000_000_000_000),
    EXABYTE(1_000_000_000_000_000_000)
}
