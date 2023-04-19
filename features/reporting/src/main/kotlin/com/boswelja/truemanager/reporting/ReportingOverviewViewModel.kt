package com.boswelja.truemanager.reporting

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.core.api.v2.reporting.ReportingGraph
import com.boswelja.truemanager.core.api.v2.reporting.ReportingV2Api
import com.boswelja.truemanager.core.api.v2.reporting.RequestedGraph
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

class ReportingOverviewViewModel(
    private val reportingV2Api: ReportingV2Api
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val reportingGraphs = MutableStateFlow<List<ReportingGraph>>(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val graphs = reportingGraphs
        .mapLatest { reportingGraphs ->
            reportingGraphs.flatMap { reportingGraph ->
                reportingGraph.toListOfGraphs()
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        refresh()
    }

    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            val reportingGraphs = reportingV2Api.getReportingGraphs(null, null, null)
            this@ReportingOverviewViewModel.reportingGraphs.value = reportingGraphs
            _isLoading.value = false
        }
    }

    private suspend fun ReportingGraph.toListOfGraphs(): List<GraphWithData> {
        return if (identifiers.isEmpty()) {
            val now = Clock.System.now()
            try {
                val graphData = reportingV2Api
                    .getGraphData(
                        graphs = listOf(RequestedGraph(name = name, identifier = null)),
                        start = now.minus(1.hours),
                        end = now
                    )
                    .first()
                listOf(
                    GraphWithData(
                        title = title,
                        entryModel = entryModelOf(
                            *graphData.data.map {
                                entriesOf(*it.map { it ?: 0.0 }.toTypedArray())
                            }.toTypedArray()
                        ),
                        verticalAxisLabel = verticalLabel,
                        legend = mapOf()
                    )
                )
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            val now = Clock.System.now()
            val graphData = reportingV2Api.getGraphData(
                graphs = identifiers.map { RequestedGraph(name, it) },
                start = now.minus(1.hours),
                end = now
            )
            graphData.map { data ->
                GraphWithData(
                    title = title.replace("{identifier}", data.identifier!!),
                    entryModel = entryModelOf(
                        *data.data.map {
                            entriesOf(*it.map { it ?: 0.0 }.toTypedArray())
                        }.toTypedArray()
                    ),
                    verticalAxisLabel = verticalLabel,
                    legend = mapOf()
                )
            }
        }
    }
}

data class GraphWithData(
    val title: String,
    val entryModel: ChartEntryModel,
    val verticalAxisLabel: String,
    val legend: Map<String, Int>
)
