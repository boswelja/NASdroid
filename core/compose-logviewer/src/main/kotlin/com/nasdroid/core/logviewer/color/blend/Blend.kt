package com.nasdroid.core.logviewer.color.blend

import com.nasdroid.core.logviewer.color.hct.Hct
import com.nasdroid.core.logviewer.color.utils.MathUtils.differenceDegrees
import com.nasdroid.core.logviewer.color.utils.MathUtils.rotationDirection
import com.nasdroid.core.logviewer.color.utils.MathUtils.sanitizeDegreesDouble

/** Functions for blending in HCT and CAM16. */
object Blend {
    /**
     * Blend the design color's HCT hue towards the key color's HCT hue, in a way that leaves the
     * original color recognizable and recognizably shifted towards the key color.
     *
     * @param designColor ARGB representation of an arbitrary color.
     * @param sourceColor ARGB representation of the main theme color.
     * @return The design color with a hue shifted towards the system's color, a slightly
     * warmer/cooler variant of the design color's hue.
     */
    fun harmonize(designColor: Int, sourceColor: Int): Int {
        val fromHct = Hct.fromInt(designColor)
        val toHct = Hct.fromInt(sourceColor)
        val differenceDegrees = differenceDegrees(fromHct.hue, toHct.hue)
        val rotationDegrees = (differenceDegrees * 0.5).coerceAtMost(15.0)
        val outputHue = sanitizeDegreesDouble(
            fromHct.hue
                    + rotationDegrees * rotationDirection(fromHct.hue, toHct.hue)
        )
        return Hct.from(outputHue, fromHct.chroma, fromHct.tone).toInt()
    }
}
