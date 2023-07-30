package com.boswelja.truemanager.apps.ui.installed.upgrade

data class UpgradeMetadata(
    val availableVersions: List<String>,
    val changelog: String,
    val imagesToBeUpdated: List<String>
)
