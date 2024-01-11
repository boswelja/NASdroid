package com.nasdroid.ui.navigation

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import kotlin.test.Test

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class NavigationModeTest {

    @Test
    fun `landscape phone returns correct NavigationMode`() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(891.dp, 411.dp))

        val navigationMode = NavigationMode.calculateFromWindowSize(windowSizeClass)

        assertEquals(
            NavigationMode(
                primaryNavigationMode = PrimaryNavigationMode.Modal,
                secondaryNavigationMode = SecondaryNavigationMode.None
            ),
            navigationMode
        )
    }

    @Test
    fun `portrait phone returns correct NavigationMode`() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(411.dp, 891.dp))

        val navigationMode = NavigationMode.calculateFromWindowSize(windowSizeClass)

        assertEquals(
            NavigationMode(
                primaryNavigationMode = PrimaryNavigationMode.Modal,
                secondaryNavigationMode = SecondaryNavigationMode.BottomNavBar
            ),
            navigationMode
        )
    }

    @Test
    fun `landscape unfolded foldable returns correct NavigationMode`() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(841.dp, 673.dp))

        val navigationMode = NavigationMode.calculateFromWindowSize(windowSizeClass)

        assertEquals(
            NavigationMode(
                primaryNavigationMode = PrimaryNavigationMode.Modal,
                secondaryNavigationMode = SecondaryNavigationMode.StartNavRail
            ),
            navigationMode
        )
    }

    @Test
    fun `portrait unfolded foldable returns correct NavigationMode`() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(673.dp, 841.dp))

        val navigationMode = NavigationMode.calculateFromWindowSize(windowSizeClass)

        assertEquals(
            NavigationMode(
                primaryNavigationMode = PrimaryNavigationMode.Modal,
                secondaryNavigationMode = SecondaryNavigationMode.StartNavRail
            ),
            navigationMode
        )
    }

    @Test
    fun `landscape tablet returns correct NavigationMode`() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(1280.dp, 900.dp))

        val navigationMode = NavigationMode.calculateFromWindowSize(windowSizeClass)

        assertEquals(
            NavigationMode(
                primaryNavigationMode = PrimaryNavigationMode.Permanent,
                secondaryNavigationMode = SecondaryNavigationMode.None
            ),
            navigationMode
        )
    }

    @Test
    fun `portrait tablet returns correct NavigationMode`() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(900.dp, 1280.dp))

        val navigationMode = NavigationMode.calculateFromWindowSize(windowSizeClass)

        assertEquals(
            NavigationMode(
                primaryNavigationMode = PrimaryNavigationMode.Permanent,
                secondaryNavigationMode = SecondaryNavigationMode.None
            ),
            navigationMode
        )
    }
}
