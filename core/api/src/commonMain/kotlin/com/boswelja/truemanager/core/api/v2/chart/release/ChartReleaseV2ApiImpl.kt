package com.boswelja.truemanager.core.api.v2.chart.release

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

internal class ChartReleaseV2ApiImpl(
    private val httpClient: HttpClient
) : ChartReleaseV2Api {
    override suspend fun getChartReleases(): List<ChartRelease> {
        val request = httpClient.get("chart/release")
        return request.body()
    }

    override suspend fun createChartRelease(newRelease: CreateChartRelease): Int {
        val request = httpClient.post("chart/release") {
            setBody(newRelease)
        }
        return request.body()
    }

    override suspend fun deleteChartRelease(id: String): Int {
        val request = httpClient.delete("chart/release/id/$id")
        return request.body()
    }

    override suspend fun getChartRelease(id: String): ChartRelease {
        val request = httpClient.get("chart/release/id/$id")
        return request.body()
    }

    override suspend fun updateChartRelease(newChartData: ChartRelease) {
        val request = httpClient.put("chart/release/id/${newChartData.id}") {
            setBody(newChartData)
        }
    }
}
