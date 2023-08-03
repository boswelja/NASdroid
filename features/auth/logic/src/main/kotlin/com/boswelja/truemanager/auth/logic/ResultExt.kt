package com.boswelja.truemanager.auth.logic

inline fun <T, R> Result<T>.then(block: (T) -> Result<R>): Result<R> = this
    .mapCatching { block(it).getOrThrow() }
