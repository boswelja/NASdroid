package com.boswelja.truemanager.apps.ui.installed.upgrade

/**
 * Describes information about the state of an apps upgrade.
 *
 * @property iconUrl The URL for the app icon.
 * @property appName The name of the app.
 * @property currentVersion The currently installed version of the app.
 * @property availableVersions A list of versions the app can upgrade to.
 * @property changelog The changelog of new app versions. This can be HTML-formatted.
 * @property imagesToBeUpdated A list of container images to be updated with the new version.
 */
data class UpgradeMetadata(
    val iconUrl: String,
    val appName: String,
    val currentVersion: String,
    val availableVersions: List<String>,
    val changelog: String,
    val imagesToBeUpdated: List<String>
)
