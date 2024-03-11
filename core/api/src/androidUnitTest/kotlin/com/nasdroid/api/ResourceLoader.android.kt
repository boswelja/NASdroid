package com.nasdroid.api

import com.nasdroid.api.v2.apiKey.ApiKeyV2ApiImplTest

actual fun readBinaryResource(resourceName: String): ByteArray {
    return ApiKeyV2ApiImplTest::class.java
        .getResourceAsStream(resourceName)!!
        .readBytes()
}
