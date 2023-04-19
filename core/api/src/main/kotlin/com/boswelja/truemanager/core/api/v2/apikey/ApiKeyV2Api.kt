package com.boswelja.truemanager.core.api.v2.apikey

import kotlinx.datetime.Instant

interface ApiKeyV2Api {

    suspend fun getAll(limit: Int?, offset: Int?, sort: String?): List<ApiKey>

    suspend fun create(name: String): String

    suspend fun delete(id: Int)

    suspend fun get(id: Int): ApiKey

    suspend fun update(id: Int, name: String)

    suspend fun reset(id: Int): String
}

data class ApiKey(
    val id: Int,
    val name: String,
    val createdAt: Instant,
    val allowList: List<AllowRule>
) {
    data class AllowRule(
        val method: String,
        val resource: String,
    )
}
