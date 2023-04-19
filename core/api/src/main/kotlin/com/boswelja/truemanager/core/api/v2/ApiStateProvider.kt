package com.boswelja.truemanager.core.api.v2

interface ApiStateProvider {
    var serverAddress: String?

    var authorization: Authorization?
}

sealed class Authorization {
    data class Basic(val username: String, val password: String) : Authorization()
    data class ApiKey(val apiKey: String) : Authorization()
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

    override var authorization: Authorization? = null
}
