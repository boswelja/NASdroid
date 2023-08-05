package com.boswelja.truemanager.auth.logic

/**
 * Chain [Result]s together. Use the result of [this], if it was successful, to produce a [Result]
 * of a new type.
 */
inline fun <T, R> Result<T>.then(block: (T) -> Result<R>): Result<R> = this
    .mapCatching { block(it).getOrThrow() }
