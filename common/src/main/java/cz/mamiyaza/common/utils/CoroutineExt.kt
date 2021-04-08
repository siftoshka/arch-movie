package cz.mamiyaza.common.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map

/**
 * Extensions mainly for Coroutines.
 */
sealed class State<out T> {
    object Loading : State<Nothing>()
    data class Error(val error: cz.mamiyaza.common.utils.Error) : State<Nothing>()
    data class Loaded<out T>(val data: T) : State<T>()
}

typealias Error = Throwable

val State<*>.error: Error?
    get() = (this as? State.Error)?.error

val <T> State<T>.value: T?
    get() = (this as? State.Loaded)?.data

typealias MutableLiveState<T> = MutableLiveData<State<T>>

val <T> MutableLiveState<T>.data: T?
    get() = (value as? State.Loaded)?.data

fun <T> MutableLiveData<State<T>>.loading() {
    value = State.Loading
}

fun <T> MutableLiveData<State<T>>.loaded(data: T) {
    value = State.Loaded(data)
}

fun <T> MutableLiveData<State<T>>.error(err: Error) {
    value = State.Error(err)
}

fun <T> MutableLiveData<State<T>>.mapLoading(): LiveData<Boolean> {
    return map(this) { it is State.Loading }
}

fun <T> MutableLiveData<State<T>>.mapLoaded(): LiveData<T> {
    return mapNotNull { it.value }
}

fun <T> MutableLiveData<State<T>>.mapError(): LiveData<Boolean> {
    return map(this) { it is State.Error }
}

fun <T, Y> LiveData<T>.mapNotNull(mapper: (T) -> Y?): LiveData<Y> {
    val mediator = MediatorLiveData<Y>()
    mediator.addSource(this) { item ->
        val mapped = mapper(item)
        if (mapped != null) {
            mediator.value = mapped
        }
    }
    return mediator
}

suspend fun <T> wrapResult(operation: suspend () -> T): Result<T> {
    return try {
        Result.success(operation())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

@Suppress("ClassName") // lowercase type names are used to match Kotlin's upcoming type definition.
sealed class Result<out T> {
    data class success<out T>(val value: T) : Result<T>()
    data class failure(val error: Exception) : Result<Nothing>()
}