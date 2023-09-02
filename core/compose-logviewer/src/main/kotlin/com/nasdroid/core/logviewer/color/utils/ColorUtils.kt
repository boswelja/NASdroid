package com.nasdroid.core.logviewer.color.utils

import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Color science utilities.
 *
 * <p>Utility methods for color science constants and color space conversions that aren't HCT or
 * CAM16.
 */
object ColorUtils {
    private val SRGB_TO_XYZ = arrayOf(
        doubleArrayOf(0.41233895, 0.35762064, 0.18051042),
        doubleArrayOf(0.2126, 0.7152, 0.0722),
        doubleArrayOf(0.01932141, 0.11916382, 0.95034478)
    )
    private val WHITE_POINT_D65 = doubleArrayOf(95.047, 100.0, 108.883)

    /** Converts a color from RGB components to ARGB format.  */
    private fun argbFromRgb(red: Int, green: Int, blue: Int): Int {
        return 255 shl 24 or (red and 255 shl 16) or (green and 255 shl 8) or (blue and 255)
    }

    /** Converts a color from linear RGB components to ARGB format.  */
    fun argbFromLinrgb(linrgb: DoubleArray): Int {
        val r = delinearized(linrgb[0])
        val g = delinearized(linrgb[1])
        val b = delinearized(linrgb[2])
        return argbFromRgb(r, g, b)
    }

    /** Returns the red component of a color in ARGB format.  */
    private fun redFromArgb(argb: Int): Int {
        return argb shr 16 and 255
    }

    /** Returns the green component of a color in ARGB format.  */
    private fun greenFromArgb(argb: Int): Int {
        return argb shr 8 and 255
    }

    /** Returns the blue component of a color in ARGB format.  */
    private fun blueFromArgb(argb: Int): Int {
        return argb and 255
    }

    /** Converts a color from XYZ to ARGB.  */
    private fun xyzFromArgb(argb: Int): DoubleArray {
        val r = linearized(redFromArgb(argb))
        val g = linearized(greenFromArgb(argb))
        val b = linearized(blueFromArgb(argb))
        return MathUtils.matrixMultiply(doubleArrayOf(r, g, b), SRGB_TO_XYZ)
    }

    /**
     * Converts an L* value to an ARGB representation.
     *
     * @param lstar L* in L*a*b*
     * @return ARGB representation of grayscale color with lightness matching L*
     */
    fun argbFromLstar(lstar: Double): Int {
        val y = yFromLstar(lstar)
        val component = delinearized(y)
        return argbFromRgb(component, component, component)
    }

    /**
     * Computes the L* value of a color in ARGB representation.
     *
     * @param argb ARGB representation of a color
     * @return L*, from L*a*b*, coordinate of the color
     */
    fun lstarFromArgb(argb: Int): Double {
        val y = xyzFromArgb(argb)[1]
        return 116.0 * labF(y / 100.0) - 16.0
    }

    /**
     * Converts an L* value to a Y value.
     *
     *
     * L* in L*a*b* and Y in XYZ measure the same quantity, luminance.
     *
     *
     * L* measures perceptual luminance, a linear scale. Y in XYZ measures relative luminance, a
     * logarithmic scale.
     *
     * @param lstar L* in L*a*b*
     * @return Y in XYZ
     */
    fun yFromLstar(lstar: Double): Double {
        return 100.0 * labInvf((lstar + 16.0) / 116.0)
    }

    /**
     * Linearizes an RGB component.
     *
     * @param rgbComponent 0 <= rgb_component <= 255, represents R/G/B channel
     * @return 0.0 <= output <= 100.0, color channel converted to linear RGB space
     */
    fun linearized(rgbComponent: Int): Double {
        val normalized = rgbComponent / 255.0
        return if (normalized <= 0.040449936) {
            normalized / 12.92 * 100.0
        } else {
            ((normalized + 0.055) / 1.055).pow(2.4) * 100.0
        }
    }

    /**
     * Delinearizes an RGB component.
     *
     * @param rgbComponent 0.0 <= rgb_component <= 100.0, represents linear R/G/B channel
     * @return 0 <= output <= 255, color channel converted to regular RGB space
     */
    private fun delinearized(rgbComponent: Double): Int {
        val normalized = rgbComponent / 100.0
        var delinearized = 0.0
        delinearized = if (normalized <= 0.0031308) {
            normalized * 12.92
        } else {
            1.055 * normalized.pow(1.0 / 2.4) - 0.055
        }
        return MathUtils.clampInt(0, 255, (delinearized * 255.0).roundToInt())
    }

    /**
     * Returns the standard white point; white on a sunny day.
     *
     * @return The white point
     */
    fun whitePointD65(): DoubleArray {
        return WHITE_POINT_D65
    }

    private fun labF(t: Double): Double {
        val e = 216.0 / 24389.0
        val kappa = 24389.0 / 27.0
        return if (t > e) {
            t.pow(1.0 / 3.0)
        } else {
            (kappa * t + 16) / 116
        }
    }

    private fun labInvf(ft: Double): Double {
        val e = 216.0 / 24389.0
        val kappa = 24389.0 / 27.0
        val ft3 = ft * ft * ft
        return if (ft3 > e) {
            ft3
        } else {
            (116 * ft - 16) / kappa
        }
    }
}
