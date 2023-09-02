package com.nasdroid.core.logviewer.color.hct

import com.nasdroid.core.logviewer.color.utils.ColorUtils.lstarFromArgb

/**
 * A color system built using CAM16 hue and chroma, and L* from L*a*b*.
 *
 * <p>Using L* creates a link between the color system, contrast, and thus accessibility. Contrast
 * ratio depends on relative luminance, or Y in the XYZ color space. L*, or perceptual luminance can
 * be calculated from Y.
 *
 * <p>Unlike Y, L* is linear to human perception, allowing trivial creation of accurate color tones.
 *
 * <p>Unlike contrast ratio, measuring contrast in L* is linear, and simple to calculate. A
 * difference of 40 in HCT tone guarantees a contrast ratio >= 3.0, and a difference of 50
 * guarantees a contrast ratio >= 4.5.
 */

/**
 * HCT, hue, chroma, and tone. A color system that provides a perceptually accurate color
 * measurement system that can also accurately render what colors will appear as in different
 * lighting environments.
 */
class Hct private constructor(argb: Int) {
    var hue = 0.0
        private set
    var chroma = 0.0
        private set
    var tone = 0.0
        private set
    private var argb = 0

    init {
        setInternalState(argb)
    }

    fun toInt(): Int {
        return argb
    }

    private fun setInternalState(argb: Int) {
        this.argb = argb
        val cam = Cam16.fromInt(argb)
        hue = cam.hue
        chroma = cam.chroma
        tone = lstarFromArgb(argb)
    }

    companion object {
        /**
         * Create an HCT color from hue, chroma, and tone.
         *
         * @param hue 0 <= hue < 360; invalid values are corrected.
         * @param chroma 0 <= chroma < ?; Informally, colorfulness. The color returned may be lower than
         * the requested chroma. Chroma has a different maximum for any given hue and tone.
         * @param tone 0 <= tone <= 100; invalid values are corrected.
         * @return HCT representation of a color in default viewing conditions.
         */
        fun from(hue: Double, chroma: Double, tone: Double): Hct {
            val argb: Int = HctSolver.solveToInt(hue, chroma, tone)
            return Hct(argb)
        }

        /**
         * Create an HCT color from a color.
         *
         * @param argb ARGB representation of a color.
         * @return HCT representation of a color in default viewing conditions
         */
        fun fromInt(argb: Int): Hct {
            return Hct(argb)
        }
    }
}
