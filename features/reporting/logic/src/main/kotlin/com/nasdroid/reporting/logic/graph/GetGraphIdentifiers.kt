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

class GetGraphIdentifiers(
    private val reportingV2Api: ReportingV2Api,
    private val graphMetadataCache: GraphMetadataCache
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(name: String): Flow<StrongResult<List<String>, ReportingIdentifiersError>> {
        return graphMetadataCache.getGraphMetadata(name)
            .mapLatest { it.identifiers }
            .onEach {
                if (it == null && !metadataRequestLock.isLocked) {
                    metadataRequestLock.withLock {
                        val metadata = reportingV2Api.getReportingGraphs(null, null, null)
                        graphMetadataCache.submitGraphMetadata(
                            metadata.map {
                                CachedGraphMetadata(
                                    name = it.name,
                                    title = it.title,
                                    verticalLabel = it.verticalLabel,
                                    identifiers = it.identifiers
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
