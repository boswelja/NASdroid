package com.nasdroid.reporting.data.metadata

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.turbine.test
import com.nasdroid.reporting.data.ReportingDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class InMemoryGraphMetadataCacheTest {

    private lateinit var driver: JdbcSqliteDriver

    private lateinit var graphMetadataCache: InMemoryGraphMetadataCache

    @BeforeTest
    fun setUp() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ReportingDatabase.Schema.create(driver)
        graphMetadataCache = InMemoryGraphMetadataCache(ReportingDatabase(driver))
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `submitGraphMetadata stores metadata that can be retrieved later`() = runTest {
        val metadata = CachedGraphMetadata(
            name = "cputemp",
            title = "CPU Temperature",
            verticalLabel = "Celsius",
            identifiers = null
        )
        graphMetadataCache.submitGraphMetadata(listOf(metadata))

        assertEquals(
            metadata,
            graphMetadataCache.getGraphMetadata(metadata.name).first()
        )
    }

    @Test
    fun `submitGraphMetadata causes getGraphMetadata to update`() = runTest {
        var metadata = CachedGraphMetadata(
            name = "cputemp",
            title = "CPU Temperature",
            verticalLabel = "Celsius",
            identifiers = null
        )
        graphMetadataCache.submitGraphMetadata(listOf(metadata))

        graphMetadataCache.getGraphMetadata(metadata.name).test {
            // Skip the first item, we know what it is
            awaitItem()

            // Add identifiers and change titles
            metadata = metadata.copy(
                identifiers = listOf("cpu1", "cpu2", "cpu3", "cpu4"),
                title = "CPU Temperature {identifier}"
            )
            graphMetadataCache.submitGraphMetadata(listOf(metadata))

            assertEquals(metadata, awaitItem())

            // Remove some identifiers
            metadata = metadata.copy(
                identifiers = listOf("cpu1", "cpu2")
            )
            graphMetadataCache.submitGraphMetadata(listOf(metadata))

            assertEquals(metadata, awaitItem())

            // Remove all identifiers, change title and vertical label
            metadata = metadata.copy(
                identifiers = null,
                title = "CPU Temperature",
                verticalLabel = "Fahrenheit"
            )
            graphMetadataCache.submitGraphMetadata(listOf(metadata))

            assertEquals(metadata, awaitItem())
        }
    }
}
