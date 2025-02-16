package com.example.infotainment_car_health_digital.di


data class DataState<out T>(
    val data: T? = null,
    val error: String? = null,
    val loading: Boolean = false,
) {
    companion object {
        fun <T> success(data: T): DataState<T> = DataState(data = data)

        fun <T> error(message: String?, data: T? = null): DataState<T> =
            DataState(error = message, data = data)

        fun <T> loading(data: T? = null): DataState<T> = DataState(data = data, loading = true)
    }
}
