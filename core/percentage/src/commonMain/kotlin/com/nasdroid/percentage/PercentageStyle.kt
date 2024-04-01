package com.nasdroid.percentage

/**
 * Possible "styles" for a percentage. For example, whether 0-100% falls on the 0-1 decimal scale.
 */
enum class PercentageStyle {
    /**
     * The "full" percentage style, where 0% == 0 and 100% == 100.
     */
    FULL,

    /**
     * The "short" percentage style, where 0% == 0 and 100% = 1.
     */
    SHORT
}
