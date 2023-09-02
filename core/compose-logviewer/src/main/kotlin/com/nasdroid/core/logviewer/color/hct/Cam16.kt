package com.nasdroid.core.logviewer.color.hct

import com.nasdroid.core.logviewer.color.utils.ColorUtils
import com.nasdroid.core.logviewer.color.utils.MathUtils
import kotlin.math.*

/**
 * CAM16, a color appearance model. Colors are not just defined by their hex code, but rather, a hex
 * code and viewing conditions.
 *
 * <p>CAM16 instances also have coordinates in the CAM16-UCS space, called J*, a*, b*, or jstar,
 * astar, bstar in code. CAM16-UCS is included in the CAM16 specification, and should be used when
 * measuring distances between colors.
 *
 * <p>In traditional color spaces, a color can be identified solely by the observer's measurement of
 * the color. Color appearance models such as CAM16 also use information about the environment where
 * the color was observed, known as the viewing conditions.
 *
 * <p>For example, white under the traditional assumption of a midday sun white point is accurately
 * measured as a slightly chromatic blue by CAM16. (roughly, hue 203, chroma 3, lightness 100)
 */
class Cam16
/**
 * All of the CAM16 dimensions can be calculated from 3 of the dimensions, in the following
 * combinations: - {j or q} and {c, m, or s} and hue - jstar, astar, bstar Prefer using a static
 * method that constructs from 3 of those dimensions. This constructor is intended for those
 * methods to use to return all possible dimensions.
 *
 * @param hue for example, red, orange, yellow, green, etc.
 * @param chroma informally, colorfulness / color intensity. like saturation in HSL, except
 * perceptually accurate.
 * @param j lightness
 * @param q brightness; ratio of lightness to white point's lightness
 * @param m colorfulness
 * @param s saturation; ratio of chroma to white point's chroma
 * @param jstar CAM16-UCS J coordinate
 * @param astar CAM16-UCS a coordinate
 * @param bstar CAM16-UCS b coordinate
 */ private constructor(
    /** Hue in CAM16  */
    // CAM16 color dimensions, see getters for documentation.
    val hue: Double,
    /** Chroma in CAM16  */
    val chroma: Double,
    /** Lightness in CAM16  */
    private val j: Double,
    /**
     * Brightness in CAM16.
     *
     *
     * Prefer lightness, brightness is an absolute quantity. For example, a sheet of white paper is
     * much brighter viewed in sunlight than in indoor light, but it is the lightest object under any
     * lighting.
     */
    private val q: Double,
    /**
     * Colorfulness in CAM16.
     *
     *
     * Prefer chroma, colorfulness is an absolute quantity. For example, a yellow toy car is much
     * more colorful outside than inside, but it has the same chroma in both environments.
     */
    private val m: Double,
    /**
     * Saturation in CAM16.
     *
     *
     * Colorfulness in proportion to brightness. Prefer chroma, saturation measures colorfulness
     * relative to the color's own brightness, where chroma is colorfulness relative to white.
     */
    private val s: Double,
    /** Lightness coordinate in CAM16-UCS  */
    // Coordinates in UCS space. Used to determine color distance, like delta E equations in L*a*b*.
    private val jstar: Double,
    /** a* coordinate in CAM16-UCS  */
    private val astar: Double,
    /** b* coordinate in CAM16-UCS  */
    private val bstar: Double
) {

    companion object {
        // Transforms XYZ color space coordinates to 'cone'/'RGB' responses in CAM16.
        val XYZ_TO_CAM16RGB = arrayOf(
            doubleArrayOf(0.401288, 0.650173, -0.051461),
            doubleArrayOf(-0.250268, 1.204414, 0.045854),
            doubleArrayOf(-0.002079, 0.048952, 0.953127)
        )

        /**
         * Create a CAM16 color from a color, assuming the color was viewed in default viewing conditions.
         *
         * @param argb ARGB representation of a color.
         */
        fun fromInt(argb: Int): Cam16 {
            return fromIntInViewingConditions(argb, ViewingConditions.DEFAULT)
        }

        /**
         * Create a CAM16 color from a color in defined viewing conditions.
         *
         * @param argb ARGB representation of a color.
         * @param viewingConditions Information about the environment where the color was observed.
         */
        // The RGB => XYZ conversion matrix elements are derived scientific constants. While the values
        // may differ at runtime due to floating point imprecision, keeping the values the same, and
        // accurate, across implementations takes precedence.
        private fun fromIntInViewingConditions(argb: Int, viewingConditions: ViewingConditions): Cam16 {
            // Transform ARGB int to XYZ
            val red = argb and 0x00ff0000 shr 16
            val green = argb and 0x0000ff00 shr 8
            val blue = argb and 0x000000ff
            val redL = ColorUtils.linearized(red)
            val greenL = ColorUtils.linearized(green)
            val blueL = ColorUtils.linearized(blue)
            val x = 0.41233895 * redL + 0.35762064 * greenL + 0.18051042 * blueL
            val y = 0.2126 * redL + 0.7152 * greenL + 0.0722 * blueL
            val z = 0.01932141 * redL + 0.11916382 * greenL + 0.95034478 * blueL
            return fromXyzInViewingConditions(x, y, z, viewingConditions)
        }

        private fun fromXyzInViewingConditions(
            x: Double, y: Double, z: Double, viewingConditions: ViewingConditions
        ): Cam16 {
            // Transform XYZ to 'cone'/'rgb' responses
            val matrix = XYZ_TO_CAM16RGB
            val rT = x * matrix[0][0] + y * matrix[0][1] + z * matrix[0][2]
            val gT = x * matrix[1][0] + y * matrix[1][1] + z * matrix[1][2]
            val bT = x * matrix[2][0] + y * matrix[2][1] + z * matrix[2][2]

            // Discount illuminant
            val rD: Double = viewingConditions.rgbD[0] * rT
            val gD: Double = viewingConditions.rgbD[1] * gT
            val bD: Double = viewingConditions.rgbD[2] * bT

            // Chromatic adaptation
            val rAF = (viewingConditions.fl * abs(rD) / 100.0).pow(0.42)
            val gAF = (viewingConditions.fl * abs(gD) / 100.0).pow(0.42)
            val bAF = (viewingConditions.fl * abs(bD) / 100.0).pow(0.42)
            val rA = sign(rD) * 400.0 * rAF / (rAF + 27.13)
            val gA = sign(gD) * 400.0 * gAF / (gAF + 27.13)
            val bA = sign(bD) * 400.0 * bAF / (bAF + 27.13)

            // redness-greenness
            val a = (11.0 * rA + -12.0 * gA + bA) / 11.0
            // yellowness-blueness
            val b = (rA + gA - 2.0 * bA) / 9.0

            // auxiliary components
            val u = (20.0 * rA + 20.0 * gA + 21.0 * bA) / 20.0
            val p2 = (40.0 * rA + 20.0 * gA + bA) / 20.0

            // hue
            val atan2 = atan2(b, a)
            val atanDegrees = MathUtils.toDegrees(atan2)
            val hue =
                if (atanDegrees < 0) atanDegrees + 360.0 else if (atanDegrees >= 360) atanDegrees - 360.0 else atanDegrees
            val hueRadians = MathUtils.toRadians(hue)

            // achromatic response to color
            val ac: Double = p2 * viewingConditions.nbb

            // CAM16 lightness and brightness
            val j = (100.0
                    * (ac / viewingConditions.aw).pow(viewingConditions.c * viewingConditions.z))
            val q: Double = ((4.0
                    / viewingConditions.c) * sqrt(j / 100.0)
                    * (viewingConditions.aw + 4.0)
                    * viewingConditions.flRoot)

            // CAM16 chroma, colorfulness, and saturation.
            val huePrime = if (hue < 20.14) hue + 360 else hue
            val eHue = 0.25 * (cos(MathUtils.toRadians(huePrime) + 2.0) + 3.8)
            val p1: Double = 50000.0 / 13.0 * eHue * viewingConditions.nc * viewingConditions.ncb
            val t = p1 * hypot(a, b) / (u + 0.305)
            val alpha = (1.64 - 0.29.pow(viewingConditions.n)).pow(0.73) * t.pow(0.9)
            // CAM16 chroma, colorfulness, saturation
            val c = alpha * sqrt(j / 100.0)
            val m: Double = c * viewingConditions.flRoot
            val s = 50.0 * sqrt(alpha * viewingConditions.c / (viewingConditions.aw + 4.0))

            // CAM16-UCS components
            val jstar = (1.0 + 100.0 * 0.007) * j / (1.0 + 0.007 * j)
            val mstar = 1.0 / 0.0228 * ln1p(0.0228 * m)
            val astar = mstar * cos(hueRadians)
            val bstar = mstar * sin(hueRadians)
            return Cam16(hue, c, j, q, m, s, jstar, astar, bstar)
        }
    }
}
