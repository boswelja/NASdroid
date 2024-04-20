package com.nasdroid.reporting.data.metadata

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.nasdroid.reporting.data.ReportingDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

/**
 * An implementation of [GraphMetadataCache] that stores data in an in-memory database.
 */
class InMemoryGraphMetadataCache(
    private val database: ReportingDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GraphMetadataCache {

    private val queries
        get() = database.graphMetadataQueries

    override suspend fun submitGraphMetadata(graphMetadata: List<CachedGraphMetadata>) {
        withContext(dispatcher) {
            queries.transaction {
                queries.removeAllMetadata()
                graphMetadata.forEach { metadata ->
                    queries.upsertGraphMetadata(
                        name = metadata.name,
                        title = metadata.title,
                        vertical_label = metadata.verticalLabel
                    )
                    queries.removeIdentifiersFor(metadata.name)
                    metadata.identifiers?.forEach { identifier ->
                        queries.upsertGraphIdentifier(
                            graph_name = metadata.name,
                            identifier = identifier
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getGraphMetadata(graphName: String): Flow<CachedGraphMetadata> {
        return queries.getMetadata(graphName).asFlow()
            .mapToList(dispatcher)
            .mapLatest { metadata ->
                val graphMetadata = metadata.first()
                CachedGraphMetadata(
                    name = graphName,
                    title = graphMetadata.title,
                    verticalLabel = graphMetadata.vertical_label,
                    identifiers = metadata
                        .mapNotNull { it.identifier }
                        .takeIf { it.isNotEmpty() }
                )
            }
    }
}
