package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.data.metadata.CachedGraphMetadata
import com.nasdroid.reporting.data.metadata.GraphMetadataCache
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Retrieves a List of identifiers for a graph, if available. An "identifier" is an option for a
 * graph that allows selecting the source of the data. For example, a graph of network activity might
 * use physical network adapters as identifiers to allow separating data by individual adapters. See
 * [invoke] for details.
 */
class GetGraphIdentifiers(
    private val reportingV2Api: ReportingV2Api,
    private val graphMetadataCache: GraphMetadataCache
) {

    /**
     * Flows either a List of graph identifiers, or a [ReportingIdentifiersError] if something went
     * wrong.
     *
     * @param name The name of the graph whose identifiers we are interested in.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(name: String): Flow<StrongResult<List<String>, ReportingIdentifiersError>> {
        return graphMetadataCache.getGraphMetadata(name)
            .mapLatest { it.identifiers }
            .onEach {
                if (it == null && !metadataRequestLock.isLocked) {
                    metadataRequestLock.withLock {
                        val metadata = reportingV2Api.getReportingGraphs(null, null, null)
                        graphMetadataCache.submitGraphMetadata(
                            metadata.map { reportingGraph ->
                                CachedGraphMetadata(
                                    name = reportingGraph.name,
                                    title = reportingGraph.title,
                                    verticalLabel = reportingGraph.verticalLabel,
                                    identifiers = reportingGraph.identifiers
                                )
                            }
                        )
                    }
                }
            }
            .mapLatest {
                if (it != null) {
                    StrongResult.success(it)
                } else {
                    StrongResult.failure(ReportingIdentifiersError.NoGroupFound)
                }
            }
    }

    companion object {
        private val metadataRequestLock = Mutex()
    }
}
