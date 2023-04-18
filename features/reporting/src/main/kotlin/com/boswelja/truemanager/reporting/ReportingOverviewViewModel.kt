package com.boswelja.truemanager.reporting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.core.api.v2.reporting.ReportingGraph
import com.boswelja.truemanager.core.api.v2.reporting.ReportingV2Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportingOverviewViewModel(
    private val reportingV2Api: ReportingV2Api
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _reportingGraphs = MutableStateFlow<List<ReportingGraph>>(emptyList())
    val reportingGraphs: StateFlow<List<ReportingGraph>> = _reportingGraphs

    init {
        refresh()
    }

    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            val reportingGraphs = reportingV2Api.getReportingGraphs(null, null, null)
            _reportingGraphs.value = reportingGraphs
            _isLoading.value = false
        }
    }
}
