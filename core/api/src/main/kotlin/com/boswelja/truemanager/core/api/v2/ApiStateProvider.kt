package com.boswelja.truemanager.core.api.v2

interface ApiStateProvider {
    var serverAddress: String?

    var sessionToken: String?
}

internal class InMemoryApiStateProvider : ApiStateProvider {
    override var serverAddress: String? = null
        set(value) {
            if (field != value) {
                if (value == null) {
                    field = null
                    return
                }

                field = "${value.removeSuffix("/")}/api/v2.0/"
            }
        }

    override var sessionToken: String? = null
}
