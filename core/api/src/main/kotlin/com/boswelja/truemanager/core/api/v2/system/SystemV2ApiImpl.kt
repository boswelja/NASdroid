package com.boswelja.truemanager.core.api.v2.system

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class SystemV2ApiImpl(
    private val httpClient: HttpClient
) : SystemV2Api {
    override suspend fun getBootId(): String {
        val response = httpClient.get("systen/boot_id")
        return response.body()
    }

    override suspend fun getEnvironment(): Environment {
        val response = httpClient.get("systen/boot_id")
        return when (val environmentDto: String = response.body()) {
            "DEFAULT" -> Environment.DEFAULT
            "EC2" -> Environment.EC2
            else -> error("Unknown environment '$environmentDto'")
        }
    }

    override suspend fun isFeatureEnabled(featureName: String): Boolean {
        val response = httpClient.post("systen/feature_enabled") {
            setBody(featureName)
        }
        return response.body()
    }

    override suspend fun getHostId(): String {
        val response = httpClient.get("system/host_id")
        return response.body()
    }

    override suspend fun getSystemInfo(): SystemInfo {
        TODO("Not yet implemented")
    }

    override suspend fun isStable(): Boolean {
        val response = httpClient.get("system/is_stable")
        return response.body()
    }

    override suspend fun updateLicense(license: String) {
        httpClient.post("systen/license_update") {
            setBody(license)
        }
    }

    override suspend fun getProductName(): String {
        val response = httpClient.get("system/product_name")
        return response.body()
    }

    override suspend fun getProductType(): String {
        val response = httpClient.get("system/product_type")
        return response.body()
    }

    override suspend fun isReady(): Boolean {
        val response = httpClient.get("system/ready")
        return response.body()
    }

    override suspend fun reboot() {
        httpClient.post("system/reboot")
    }

    override suspend fun shutdown() {
        httpClient.post("system/shutdown")
    }

    override suspend fun getState(): State {
        TODO("Not yet implemented")
    }

    override suspend fun getVersion(): String {
        val response = httpClient.get("system/version")
        return response.body()
    }

    override suspend fun getShortVersion(): String {
        val response = httpClient.get("system/version_short")
        return response.body()
    }
}
