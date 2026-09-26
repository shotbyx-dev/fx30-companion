package com.fx30companion.app.data

/**
 * Shooting-scenario recipes for the Sony FX30.
 * Content distilled from Sony's official Help Guide, Sony cinematography
 * articles, and independent expert guides (see README for sources).
 */
data class Scenario(
    val id: String,
    val title: String,
    val subtitle: String,
    val mode: String,
    val codec: String,
    val resolution: String,
    val shutter: String,
    val aperture: String,
    val iso: String,
    val whiteBalance: String,
    val pictureProfile: String,
    val audio: String,
    val stabilization: String,
    val tips: List<String>,
    val warnings: List<String>
)

val scenarios: List<Scenario> = listOf(
    Scenario(
        id = "cinematic",
        title = "Cinematic 24p Narrative",
        subtitle = "Short films, music videos, client work",
        mode = "Manual (M) movie — Cine EI or Flexible ISO",
        codec = "XAVC S-I 4K 240M (max quality) or XAVC HS 4K 100M 4:2:2 10-bit",
        resolution = "3840 x 2160, 23.98p (true 24.00p + DCI 4096 x 2160 possible)",
        shutter = "180° angle (= 1/48 s at 24p) — firmware v5.00+",
        aperture = "f/2.8 – f/4 for controlled depth of field",
        iso = "Base ISO 800 (Cine EI); Flexible ISO if light changes",
        whiteBalance = "Manual — 5600K daylight / 3200K tungsten. Lock it.",
        pictureProfile = "S-Log3 (Cine EI) or PP8 — monitor with an s709 LUT",
        audio = "Manual levels, headphones on; XLR handle for boom/lav",
        stabilization = "Tripod or gimbal; IBIS Standard (or Off on sticks)",
        tips = listOf(
            "Expose to the right: +1.7 to +2.0 stops on the meter without clipping highlights.",
            "Set Zebra 1 to 100+ as a clipping alarm and Zebra 2 to 70 for skin.",
            "Use a Display LUT (Sony s709) on the LCD instead of Gamma Display Assist alone."
        ),
        warnings = listOf(
            "No internal ND — bring a variable ND filter to hold 180° in daylight."
        )
    ),
    Scenario(
        id = "vlog",
        title = "Vlog / Run-and-Gun",
        subtitle = "YouTube, travel vlogs, handheld documentary",
        mode = "Manual or flexible auto — Cine EI Quick or Flexible ISO",
        codec = "XAVC HS 4K 75M 4:2:0 10-bit (small files) or 100M 4:2:2",
        resolution = "4K 29.97p / 25p",
        shutter = "180° (= 1/60 s at 30p)",
        aperture = "f/4 – f/8 for deep, forgiving focus",
        iso = "Auto ISO limited to 125 – 6400 (S-Cinetone base ISOs are 125 / 400)",
        whiteBalance = "AWB with lock, or manual 5600K outdoors",
        pictureProfile = "PP11 S-Cinetone — great color with zero grading",
        audio = "On-camera shotgun or built-in mic + wind filter; manual levels",
        stabilization = "Active IBIS (adds ~1.13x crop — use a wide lens, e.g. 11–15mm APS-C)",
        tips = listOf(
            "S-Cinetone exposes at ±0.0 on the meter — no ETTR needed.",
            "Active IBIS plus a wide lens is the handheld vlogging combo."
        ),
        warnings = listOf(
            "Active mode crops in — frame wider than you think you need."
        )
    ),
    Scenario(
        id = "lowlight",
        title = "Low Light",
        subtitle = "Night exteriors, dim interiors, events",
        mode = "Manual — Flexible ISO (NOT Cine EI)",
        codec = "XAVC HS 4K 75M 4:2:0 10-bit",
        resolution = "4K 23.98p",
        shutter = "180° (or 360° for one extra stop in extremes)",
        aperture = "Widest available — f/1.4 – f/2.8",
        iso = "High base ISO 2500, then raise freely — a correctly exposed ISO 12800 beats an underexposed ISO 2500",
        whiteBalance = "Manual (AWB hunts in mixed light) — 3200–4500K typical",
        pictureProfile = "S-Log3 in Flexible ISO with ETTR; or PP Off if noise is unmanageable",
        audio = "Manual levels — avoid auto gain pumping in quiet rooms",
        stabilization = "IBIS Standard (Active crops away light you need); tripod preferred",
        tips = listOf(
            "Cine EI locks you to base ISO — Flexible ISO is the low-light mode.",
            "Bright log grades clean; dark log grades noisy. When in doubt, add light or open up."
        ),
        warnings = listOf(
            "Staying at base ISO 800 in the dark is the #1 beginner mistake."
        )
    ),
    Scenario(
        id = "slowmo",
        title = "Slow Motion 120fps",
        subtitle = "Sports, action cutaways, dreamy B-roll",
        mode = "S&Q or normal movie at 119.88p",
        codec = "XAVC HS 4K 280M 4:2:2 10-bit or XAVC S 4K 280M (S-I is unavailable at 120p)",
        resolution = "3840 x 2160, 119.88p — with a ~1.6x crop!",
        shutter = "180° (= 1/240 s at 120p) — bring extra light",
        aperture = "f/2.8 – f/5.6",
        iso = "Base 800, or 2500 in dim light",
        whiteBalance = "Manual WB, locked before the take",
        pictureProfile = "S-Log3 (Cine EI Quick) or PP11 S-Cinetone for fast turnaround",
        audio = "Reference audio only — it gets slowed down in post",
        stabilization = "IBIS Standard; gimbal for movement",
        tips = listOf(
            "120p needs a lot of light — fast shutter speeds eat exposure.",
            "Slowed footage hides rolling shutter skew, but avoid whip pans anyway."
        ),
        warnings = listOf(
            "The ~1.6x crop at 4K120 surprises everyone — bring wider glass.",
            "XAVC S-I tops out at 60p, so don't plan an All-Intra slow-mo workflow."
        )
    ),
    Scenario(
        id = "interview",
        title = "Interview",
        subtitle = "Talking heads, testimonials, corporate",
        mode = "Manual — Cine EI or Flexible ISO",
        codec = "XAVC S-I 4K 240M, or XAVC HS 4K 50–100M 4:2:2 10-bit for long takes",
        resolution = "4K 23.98p",
        shutter = "180° (1/48 s)",
        aperture = "f/2.8 – f/4 — subject separation with forgiving focus",
        iso = "Base ISO 800 with controlled lighting",
        whiteBalance = "Manual, matched to the key light (e.g. 5600K) — grey-card it",
        pictureProfile = "S-Log3 + s709 monitoring LUT, or PP11 S-Cinetone",
        audio = "XLR handle: lav + boom, manual levels, headphones, USB-PD power for long takes",
        stabilization = "Tripod; IBIS Off or Standard",
        tips = listOf(
            "Light the face so skin sits at 48–52% IRE on a waveform (or pink in false color).",
            "Lock white balance before enabling log — fixing WB on S-Log3 in post is painful."
        ),
        warnings = listOf(
            "AWB drifting mid-interview will ruin a take — always manual."
        )
    ),
    Scenario(
        id = "travel",
        title = "Travel",
        subtitle = "One-bag trips, family, fast-moving days",
        mode = "Manual — Flexible ISO",
        codec = "XAVC HS 4K 75M 4:2:0 10-bit (storage-efficient)",
        resolution = "4K 29.97p / 25p (59.94p for action)",
        shutter = "180°",
        aperture = "f/5.6 – f/8 daytime with variable ND; f/2.8 in the evening",
        iso = "Auto ISO capped 125 – 6400",
        whiteBalance = "AWB or manual 5600K",
        pictureProfile = "PP11 S-Cinetone — no-grade workflow",
        audio = "On-camera mic + windscreen; manual levels",
        stabilization = "Active IBIS handheld; Standard on tripod",
        tips = listOf(
            "XAVC HS keeps a full travel day on one card.",
            "S-Cinetone means footage is ready to share the same night."
        ),
        warnings = listOf(
            "Variable ND is mandatory — the FX30 has no internal ND."
        )
    )
)
