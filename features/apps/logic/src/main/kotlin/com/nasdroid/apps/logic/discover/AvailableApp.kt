package com.nasdroid.apps.logic.discover

import kotlinx.datetime.Instant

/**
 * Describes an application that can be installed on the system.
 *
 * @property id The ID of the app. Unique within the [catalogName] providing the app.
 * @property title The human-readable title of the app.
 * @property description A brief, human-readable description of the app.
 * @property iconUrl The URL for the app icon.
 * @property version The latest app version.
 * @property catalogName The name of the catalog that provides this app.
 * @property catalogTrain The train in the catalog that provides this app.
 * @property isInstalled Whether an instance of the app is already installed on the system.
 * @property lastUpdated The exact instant when this app was last updated.
 */
data class AvailableApp(
    val id: String,
    val title: String,
    val description: String,
    val iconUrl: String,
    val version: String,
    val catalogName: String,
    val catalogTrain: String,
    val isInstalled: Boolean,
    val lastUpdated: Instant,
) {
    companion object {
        internal fun com.nasdroid.api.v2.app.AvailableApp.toSanitizedModel(): AvailableApp {
            return AvailableApp(
                id = name,
                title = title,
                description = description,
                iconUrl = requireNotNull(iconUrl) {
                    "An icon is required for applications, but one wasn't found in\n$this"
                },
                version = latestAppVersion,
                catalogName = catalog,
                catalogTrain = train,
                isInstalled = installed,
                lastUpdated = Instant.fromEpochMilliseconds(lastUpdate)
            )
        }
    }
}
