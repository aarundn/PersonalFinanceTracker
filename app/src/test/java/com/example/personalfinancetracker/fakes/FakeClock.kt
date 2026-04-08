package com.example.personalfinancetracker.fakes

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * A standard Fake Clock for testing.
 * By default, it is frozen at a specific moment in time (e.g. 12:00 PM on a specific date).
 * This guarantees that calculations like "daysPassed" or "Good morning/afternoon" 
 * never cause flaky tests just because the test runs on a different day/time.
 */
class FakeClock(
    var frozenInstant: Instant = Instant.parse("2024-01-15T12:00:00Z")
) : Clock {
    
    override fun now(): Instant {
        return frozenInstant
    }
}
