package com.boswelja.capacity

import com.boswelja.capacity.Capacity.Companion.bytes
import com.boswelja.capacity.Capacity.Companion.gigabytes
import com.boswelja.capacity.Capacity.Companion.kibibytes
import com.boswelja.capacity.Capacity.Companion.tebibytes
import com.boswelja.capacity.Capacity.Companion.terabytes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CapacityTest {

    @Test
    fun toLong_roundsDownCorrectly() {
        assertEquals(
            1,
            1.5.terabytes.toLong(CapacityUnit.TEBIBYTE)
        )
        assertEquals(
            1,
            1.49.terabytes.toLong(CapacityUnit.TERABYTE)
        )
    }

    @Test
    fun toLong_roundsUpCorrectly() {
        assertEquals(
            2,
            1.5.tebibytes.toLong(CapacityUnit.TERABYTE)
        )
        assertEquals(
            2,
            1.5.terabytes.toLong(CapacityUnit.TERABYTE)
        )
    }

    @Test
    fun instantiation() {
        assertEquals(
            0.bytes,
            Capacity(0)
        )
        assertEquals(
            0f.gigabytes,
            Capacity(0)
        )
        assertEquals(
            0.0.kibibytes,
            Capacity(0)
        )
        assertEquals(
            Long.MAX_VALUE.bytes,
            Capacity(Long.MAX_VALUE)
        )
        assertEquals(
            Long.MIN_VALUE.bytes,
            Capacity(Long.MIN_VALUE)
        )
    }

    @Test
    fun compareTo_comparesSameSizes() {
        assertTrue(
            1.5.terabytes.compareTo(1500.gigabytes) == 0
        )
        assertTrue(
            1500.gigabytes.compareTo(1.5.terabytes) == 0
        )
    }

    @Test
    fun compareTo_comparesDifferentSizes() {
        assertTrue(
            1.5.terabytes > 1.gigabytes
        )
        assertTrue(
            1.gigabytes < 1.5.terabytes
        )
    }
}
