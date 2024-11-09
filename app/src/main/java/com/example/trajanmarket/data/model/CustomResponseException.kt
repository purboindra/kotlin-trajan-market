package com.example.trajanmarket.data.model

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable

@Serializable
data class Error(val code: Int, val message: String)
class CustomResponseException(response: HttpResponse, cachedResponseText: String) :
    ResponseException(response, cachedResponseText) {
    
    override val message: String =
        "Custom server error: ${response.call.request.url}. " + "Status: ${response.status}. Text: \"${cachedResponseText}\""
}