package com.nasdroid.api.v2.system

import com.nasdroid.api.websocket.system.Environment
import com.nasdroid.api.websocket.system.State
import com.nasdroid.api.websocket.system.SystemInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class SystemV2ApiImpl(
    private val httpClient: HttpClient
) : SystemV2Api {
    override suspend fun getBootId(): String {
        val response = httpClient.get("system/boot_id")
        return response.body()
    }

    override suspend fun getEnvironment(): Environment {
        val response = httpClient.get("system/boot_id")
        return response.body()
    }

    override suspend fun isFeatureEnabled(featureName: String): Boolean {
        val response = httpClient.post("system/feature_enabled") {
            setBody(featureName)
        }
        return response.body()
    }

    override suspend fun getHostId(): String {
        val response = httpClient.get("system/host_id")
        return response.body()
    }

    override suspend fun getSystemInfo(): SystemInfo {
        val response = httpClient.get("system/info")
        return response.body()
    }

    override suspend fun isStable(): Boolean {
        val response = httpClient.get("system/is_stable")
        return response.body()
    }

    override suspend fun updateLicense(license: String) {
        httpClient.post("system/license_update") {
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
        val response = httpClient.get("system/state")
        return response.body()
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
