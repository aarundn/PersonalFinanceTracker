package com.example.data.remote.frankfurter

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Ktor-based service for the Frankfurter exchange-rate API.
 *
 * Receives a pre-configured [HttpClient] via DI — keeps the adapter
 * decoupled from HTTP engine and serialization setup.
 */
class FrankfurterApiService(private val client: HttpClient) {

    /**
     * Fetches the latest rate from [from] to [to].
     *
     * @see [Frankfurter docs](https://www.frankfurter.app/docs/)
     */
    suspend fun getLatestRate(from: String, to: String): FrankfurterResponse {
        return client.get("$BASE_URL/latest") {
            parameter("from", from)
            parameter("to", to)
        }.body()
    }

    companion object {
        private const val BASE_URL = "https://api.frankfurter.app"
    }
}
