package com.example.conversion_rate.domain.port

import java.math.BigDecimal

/**
 * Port (interface) that each exchange rate API adapter must implement.
 *
 * The conversion-rate module owns this contract.
 * Concrete adapters (Frankfurter, ExchangeRate-API, etc.) live in the app module
 * and depend on this port.
 */
interface ExchangeRateProviderPort {

    /** Unique machine-readable id, e.g. "frankfurter_api". */
    val id: String

    /** Human-readable label shown in the Settings picker, e.g. "Frankfurter". */
    val displayName: String

    /**
     * Fetches the exchange rate from [fromCurrencyCode] to [toCurrencyCode].
     *
     * @return [Result.success] with the rate as a [BigDecimal],
     *         or [Result.failure] with the error.
     */
    suspend fun getRate(fromCurrencyCode: String, toCurrencyCode: String): Result<BigDecimal>
}
