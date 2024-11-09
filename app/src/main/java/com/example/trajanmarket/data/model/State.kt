package com.example.trajanmarket.data.model

sealed class State<out T> {
    data object Idle : State<Nothing>()
    data class Succes<T>(val data: T) : State<T>()
    data object Loading : State<Nothing>()
    data class Failure(val throwable: Throwable) : State<Nothing>()
}