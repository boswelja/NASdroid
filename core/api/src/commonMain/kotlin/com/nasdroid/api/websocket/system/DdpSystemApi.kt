package com.nasdroid.api.websocket.system

import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.ddp.callMethod
import kotlin.time.Duration

class DdpSystemApi(
    private val client: DdpWebsocketClient,
) : SystemApi {
    override suspend fun bootId(): String {
        return client.callMethod("system.boot_id")
    }

    override suspend fun buildTime(): String {
        return client.callMethod("system.build_time")
    }

    override suspend fun debug(): Int {
        return client.callMethod("system.debug")
    }

    override suspend fun featureEnabled(feature: Feature): Boolean {
        return client.callMethod("system.feature_enabled", listOf(feature))
    }

    override suspend fun hostId(): String {
        return client.callMethod("system.host_id")
    }

    override suspend fun info(): SystemInfo {
        return client.callMethod("system.info")
    }

    override suspend fun isStable(): String {
        return client.callMethod("system.is_stable")
    }

    override suspend fun licenseUpdate(license: String) {
        return client.callMethod("system.license_update", listOf(license))
    }

    override suspend fun productType(): String {
        return client.callMethod("system.product_type")
    }

    override suspend fun ready(): Boolean {
        return client.callMethod("system.ready")
    }

    override suspend fun reboot(delay: Duration): Int {
        return client.callMethod("system.reboot", listOf(mapOf("delay" to delay.inWholeSeconds)))
    }

    override suspend fun releaseNotesUrl(version: String?): String? {
        return client.callMethod("system.release_notes_url", listOf(version))
    }

    override suspend fun shutdown(delay: Duration): Int {
        return client.callMethod("system.shutdown", listOf(mapOf("delay" to delay.inWholeSeconds)))
    }

    override suspend fun state(): State {
        return client.callMethod("system.state")
    }

    override suspend fun version(): String {
        return client.callMethod("system.version")
    }

    override suspend fun versionShort(): String {
        return client.callMethod("system.version_short")
    }
}