package com.nasdroid.reporting.data.metadata

import kotlinx.coroutines.flow.Flow

/**
 * Allows managing a cache that contains [CachedGraphMetadata].
 */
interface GraphMetadataCache {

    /**
     * Submits a list of [CachedGraphMetadata] to be stored in the cache. All existing metadata will
     * be removed before the new metadata is added.
     *
     * @param graphMetadata The list of metadata to submit for storage.
     */
    suspend fun submitGraphMetadata(graphMetadata: List<CachedGraphMetadata>)

    /**
     * Retrieves the current [CachedGraphMetadata] from the cache as a [Flow].
     *
     * @param graphName The name of the graph to retrieve metadata for.
     *
     * @return A flow that emits the current [CachedGraphMetadata] when it's ready, as well as any
     * future metadata updates.
     */
    fun getGraphMetadata(graphName: String): Flow<CachedGraphMetadata>
}

/**
 * A data class representing cached graph metadata.
 *
 * @property name The unique identifier of the graph.
 * @property title The human-readable name of the graph. This might contain `{identifier}`, which
 * should be replaced with the identifier that the graph is for.
 * @property verticalLabel The label to be displayed on the y-axis of the graph.
 * @property identifiers A list of identifiers associated with the graph.
 */
data class CachedGraphMetadata(
    val name: String,
    val title: String,
    val verticalLabel: String,
    val identifiers: List<String>?
)
