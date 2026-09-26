package com.fx30companion.app.data

import androidx.compose.ui.graphics.Color
import kotlin.math.log10
import kotlin.math.pow

/**
 * Exposure math and reference data for the Sony FX30's S-Log3 gamma.
 *
 * Anchors (Sony cinematography docs, Alister Chapman):
 *  - 18% middle grey  -> 41% IRE  (10-bit code value 420)
 *  - Average skin     -> 48–52% IRE
 *  - 90% white card  -> ~61% IRE
 *  - Hard clip        -> ~93–94% IRE
 *
 * The curve below is an illustrative approximation that is pinned to the
 * 18%-grey anchor; it is used to make the interactive scopes move smoothly,
 * not as a calibration reference.
 */
object ExposureMath {

    /** S-Log3 OETF approximation -> 10-bit code value for scene-linear [x]. 0.18 = middle grey. */
    fun sLog3Code(x: Double): Double {
        val xc = x.coerceAtLeast(0.0)
        val atThreshold = 420.0 + 261.5 * log10((0.01125 + 0.01) / 0.19)
        return if (xc < 0.01125) {
            // linear toe, pinned continuous at the threshold
            95.0 + (atThreshold - 95.0) * (xc / 0.01125)
        } else {
            420.0 + 261.5 * log10((xc + 0.01) / 0.19)
        }
    }

    /** IRE (%) for a scene-linear value. */
    fun ire(x: Double): Double = (sLog3Code(x) / 1023.0 * 100.0).coerceIn(0.0, 109.0)

    /** IRE (%) for a value expressed in stops relative to 18% grey, with exposure offset. */
    fun ireAtStops(stopsRelGrey: Double, exposureEv: Double = 0.0): Double =
        ire(0.18 * 2.0.pow(stopsRelGrey + exposureEv))

    /** Rec.709 OETF (for the comparison curve), output 0..1. */
    fun rec709(x: Double): Double {
        val xc = x.coerceAtLeast(0.0)
        return if (xc < 0.018) 4.5 * xc else 1.099 * xc.pow(0.45) - 0.099
    }
}

/** A simulated scene zone, expressed in stops relative to 18% grey. */
data class SceneZone(
    val label: String,
    val stopsRelGrey: Double,
    val weight: Double
)

/** The demo scene used by the interactive waveform and histogram. */
val demoScene: List<SceneZone> = listOf(
    SceneZone("Deep shadow", -5.0, 0.12),
    SceneZone("Shadow", -3.0, 0.18),
    SceneZone("Background", -1.5, 0.20),
    SceneZone("18% grey card", 0.0, 0.08),
    SceneZone("Skin tone", 1.2, 0.20),
    SceneZone("Bright sky", 2.5, 0.14),
    SceneZone("Specular highlight", 4.5, 0.08)
)

data class IreTarget(
    val label: String,
    val ire: Double,
    val bandTop: Double? = null,
    val note: String
)

/** The "how it should look" reference targets for S-Log3 on a waveform. */
val slog3Targets: List<IreTarget> = listOf(
    IreTarget("Hard clip", 93.5, null, "Nothing important should touch this. Zebra 100+ warns you."),
    IreTarget("90% white card", 61.0, null, "Expose a white card here with zebras at 61% (narrow window)."),
    IreTarget("Skin tones", 50.0, 52.0, "Average skin lives at 48–52% IRE in S-Log3. Band shown 48–52."),
    IreTarget("18% middle grey", 41.0, null, "The anchor of S-Log3. Grey card + 41% zebra = perfect base exposure.")
)

data class FalseColorBand(
    val name: String,
    val ireRange: String,
    val color: Color,
    val meaning: String
)

/** Atomos-style false color mapping (assumes a Rec.709-ish monitoring signal). */
val falseColorBands: List<FalseColorBand> = listOf(
    FalseColorBand("Magenta", "< 10 IRE", Color(0xFFFF00FF), "Underexposed — no usable detail"),
    FalseColorBand("Blue", "10 – 20 IRE", Color(0xFF2266FF), "Near black / crushed"),
    FalseColorBand("Dark gray", "20 – 40 IRE", Color(0xFF4A4A4A), "Shadows"),
    FalseColorBand("Green", "41 – 47 IRE", Color(0xFF2ECC40), "18% grey — correct midtone"),
    FalseColorBand("Pink", "48 – 58 IRE", Color(0xFFFF6EC7), "Skin tones — correct"),
    FalseColorBand("Light gray", "60 – 75 IRE", Color(0xFFBDBDBD), "Bright midtones"),
    FalseColorBand("Yellow", "80 – 90 IRE", Color(0xFFFFDC00), "Near white — hot"),
    FalseColorBand("Red", "90 – 100+ IRE", Color(0xFFFF4136), "Clipping — overexposed")
)

/** Look up which false-color band an IRE value falls in. */
fun falseColorBandFor(ire: Double): FalseColorBand {
    val bands = listOf(
        10.0 to falseColorBands[0], 20.0 to falseColorBands[1],
        40.0 to falseColorBands[2], 47.0 to falseColorBands[3],
        58.0 to falseColorBands[4], 75.0 to falseColorBands[5],
        90.0 to falseColorBands[6]
    )
    return bands.firstOrNull { ire < it.first }?.second ?: falseColorBands[7]
}

data class ZebraGuide(
    val zebra: String,
    val level: String,
    val purpose: String
)

val zebraSetup: List<ZebraGuide> = listOf(
    ZebraGuide("Zebra 1 (custom)", "100+", "Clipping alarm — if it appears on anything important, pull exposure down."),
    ZebraGuide("Zebra 2", "70", "Skin-tone check — stripes on faces mean skin is bright and healthy."),
    ZebraGuide("Narrow window", "2 – 6%", "In S-Log3 each stop spans only ~8% IRE between grey and white — a wide zebra window hides ~1 stop of error."),
    ZebraGuide("With s709 LUT", "Grey 44–45 · Skin 57–62 · White 77–78", "Targets shift when monitoring through a conversion LUT.")
)

data class ShutterPreset(val fpsLabel: String, val shutterSpeed: String)

val shutterPresets: List<ShutterPreset> = listOf(
    ShutterPreset("23.98 fps", "1/48 s"),
    ShutterPreset("29.97 fps", "1/60 s"),
    ShutterPreset("59.94 fps", "1/120 s"),
    ShutterPreset("119.88 fps", "1/240 s")
)
