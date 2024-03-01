package com.omarahmedd.currencyapp.domain

import kotlinx.coroutines.yield

/**
 * A generic class that holds a value.
 * @param <T>
 */
sealed class UCResult<out R> {

    data class Success<out T>(val data: T) : UCResult<T>()
    data class Error(val throwable: Throwable) : UCResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[throwable=$throwable]"
        }
    }

    fun successOrThrow(): R {
        return when (this) {
            is Success<R> -> data
            is Error -> throw throwable
        }
    }
}

/**
 * `true` if [UCResult] is of type [Success] & holds non-null [Success.data].
 */
val UCResult<*>.succeeded
    get() = this is UCResult.Success && data != null

suspend fun <T> UCResult<T>.yieldOrReturn(): UCResult<T> {
    yield()
    return this
}
