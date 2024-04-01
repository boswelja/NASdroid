package com.nasdroid.percentage

import com.nasdroid.percentage.Percentage.Companion.percent
import kotlin.test.Test
import kotlin.test.assertEquals

class PercentageTest {
    data class TestPercentage(
        val intValue: Int,
        val shortFloat: Float,
        val fullFloat: Float,
        val shortDouble: Double,
        val fullDouble: Double
    )

    val ZERO_PERCENT = TestPercentage(
        intValue = 0,
        shortFloat = 0.0f,
        fullFloat = 0.0f,
        shortDouble = 0.0,
        fullDouble = 0.0
    )

    val ONE_HUNDRED_PERCENT = TestPercentage(
        intValue = 100,
        shortFloat = 1.0f,
        fullFloat = 100.0f,
        shortDouble = 1.0,
        fullDouble = 100.0
    )

    val TWO_HUNDRED_PERCENT = TestPercentage(
        intValue = 200,
        shortFloat = 2.0f,
        fullFloat = 200.0f,
        shortDouble = 2.0,
        fullDouble = 200.0
    )

    @Test
    fun `100 percent is mapped correctly`() {
        testValue(
            1.0f.percent,
            ONE_HUNDRED_PERCENT
        )
        testValue(
            100.0.percent,
            ONE_HUNDRED_PERCENT
        )
        testValue(
            100.percent,
            ONE_HUNDRED_PERCENT
        )
    }

    @Test
    fun `200 percent is mapped correctly`() {
        testValue(
            2.0f.percent,
            TWO_HUNDRED_PERCENT
        )
        testValue(
            200.0.percent,
            TWO_HUNDRED_PERCENT
        )
        testValue(
            200.percent,
            TWO_HUNDRED_PERCENT
        )
    }

    @Test
    fun `0 percent is mapped correctly`() {
        testValue(
            0f.percent,
            ZERO_PERCENT
        )
        testValue(
            0.0.percent,
            ZERO_PERCENT
        )
        testValue(
            0.percent,
            ZERO_PERCENT
        )
    }

    private fun testValue(percentage: Percentage, testPoints: TestPercentage) {
        assertEquals(
            testPoints.intValue,
            percentage.toInt()
        )
        assertEquals(
            testPoints.shortFloat,
            percentage.toFloat(PercentageStyle.SHORT)
        )
        assertEquals(
            testPoints.fullFloat,
            percentage.toFloat(PercentageStyle.FULL)
        )
        assertEquals(
            testPoints.shortDouble,
            percentage.toDouble(PercentageStyle.SHORT)
        )
        assertEquals(
            testPoints.fullDouble,
            percentage.toDouble(PercentageStyle.FULL)
        )
    }
}
