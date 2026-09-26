package com.fx30companion.app

import com.fx30companion.app.data.ExposureMath
import com.fx30companion.app.data.commonMistakes
import com.fx30companion.app.data.falseColorBandFor
import com.fx30companion.app.data.keySpecs
import com.fx30companion.app.data.pictureProfiles
import com.fx30companion.app.data.scenarios
import com.fx30companion.app.data.slog3Targets
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

/**
 * JVM unit tests for the exposure math and content integrity.
 * These run in CI on every push (./gradlew testDebugUnitTest).
 */
class ExposureMathTest {

    @Test
    fun middleGrey_mapsTo41Ire() {
        // Sony's anchor: 18% grey -> 10-bit code value 420 -> 41.05% IRE
        assertTrue(abs(ExposureMath.ire(0.18) - 41.05) < 0.5)
    }

    @Test
    fun curve_isMonotonic() {
        var prev = -1.0
        for (i in 0..200) {
            val v = ExposureMath.ire(i / 200.0 * 4.0)
            assertTrue("not monotonic at step $i", v >= prev)
            prev = v
        }
    }

    @Test
    fun skinZone_sitsInTargetBand() {
        // demoScene models skin at +1.2 stops over grey -> should land ~48-52 IRE
        val skin = ExposureMath.ireAtStops(1.2, 0.0)
        assertTrue("skin at $skin IRE", skin in 47.0..53.0)
    }

    @Test
    fun exposureShift_movesSkinUp() {
        val base = ExposureMath.ireAtStops(1.2, 0.0)
        val pushed = ExposureMath.ireAtStops(1.2, 1.7)
        assertTrue(pushed > base)
    }

    @Test
    fun falseColor_skinIsPink() {
        assertEquals("Pink", falseColorBandFor(50.0).name)
    }

    @Test
    fun falseColor_greyIsGreen() {
        assertEquals("Green", falseColorBandFor(44.0).name)
    }

    @Test
    fun falseColor_clipIsRed() {
        assertEquals("Red", falseColorBandFor(95.0).name)
    }

    @Test
    fun scenarios_haveFullContent() {
        assertEquals(6, scenarios.size)
        scenarios.forEach { s ->
            assertTrue("${s.id} missing tips", s.tips.isNotEmpty())
            assertTrue("${s.id} missing codec", s.codec.isNotBlank())
        }
    }

    @Test
    fun referenceData_isComplete() {
        assertEquals(12, pictureProfiles.size) // PP1..PP11 + PPLUT
        assertEquals(14, commonMistakes.size)
        assertTrue(keySpecs.isNotEmpty())
        assertEquals(4, slog3Targets.size)
    }
}
