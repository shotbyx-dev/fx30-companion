package com.fx30companion.app.data

/** Picture Profile defaults on the FX30 (verified against Sony's Help Guide). */
data class PictureProfile(
    val id: String,
    val gamma: String,
    val colorMode: String,
    val useFor: String,
    val highlight: Boolean = false
)

val pictureProfiles: List<PictureProfile> = listOf(
    PictureProfile("PP1", "Movie", "Standard", "General video, standard look"),
    PictureProfile("PP2", "Still", "Standard", "Still-photo-like gamma"),
    PictureProfile("PP3", "ITU709", "Natural color tone", "Natural Rec.709 look"),
    PictureProfile("PP4", "ITU709(800%)", "ITU709 Matrix", "Broadcast-faithful 709 with highlight headroom"),
    PictureProfile("PP5", "Cine1", "Cinema", "Filmic, gentle contrast in shadows"),
    PictureProfile("PP6", "Cine2", "Cinema", "Filmic, optimized for 100% signal"),
    PictureProfile("PP7", "S-Log2", "S-Gamut", "Legacy log (friendlier in 8-bit)"),
    PictureProfile("PP8", "S-Log3", "S-Gamut3.Cine", "Max dynamic range — the easiest Sony log to grade", highlight = true),
    PictureProfile("PP9", "S-Log3", "S-Gamut3", "Max DR, wider gamut — harder to grade, good for ACES/archival"),
    PictureProfile("PP10", "HLG2", "BT.2020 / 709", "HDR recording (HLG = ITU-R BT.2100)"),
    PictureProfile("PP11", "S-Cinetone", "Cinema", "Cinematic color straight out of camera — no grading", highlight = true),
    PictureProfile("PPLUT1–4", "User LUT", "Custom", "Bake a custom look into the recording or apply to monitor only")
)

data class Mistake(val title: String, val fix: String)

val commonMistakes: List<Mistake> = listOf(
    Mistake("Underexposing S-Log3", "Fix: ETTR +1.7 to +2.0 stops. Bright log grades clean; dark log grades noisy."),
    Mistake("Judging exposure from the flat LCD image", "Fix: use zebras + a monitoring LUT. Never trust the Multi meter in contrasty scenes."),
    Mistake("Staying at base ISO 800 in the dark", "Fix: switch to the high base ISO 2500. Correct exposure beats native ISO."),
    Mistake("Using Cine EI for low light or run-and-gun", "Fix: Cine EI locks base ISO — use Flexible ISO or Cine EI Quick when light changes fast."),
    Mistake("S-Log3 in 8-bit record modes", "Fix: always shoot 10-bit for log, or it will band. Use PP Off / S-Cinetone for quick turnaround."),
    Mistake("Shooting log with no grading plan", "Fix: PP11 S-Cinetone gives ready-to-deliver color with no grade."),
    Mistake("Gamma Display Assist as the only exposure judge", "Fix: it causes chronic underexposure — pair it with zebras or a real LUT."),
    Mistake("Forgetting the ~1.6x crop at 4K120", "Fix: bring wider glass for slow motion."),
    Mistake("Stacking Active IBIS crop unexpectedly", "Fix: Standard IBIS on tripod; Active only handheld."),
    Mistake("Whip pans and fast action", "Fix: the FX30 has slow sensor readout — move slowly; 120p reduces visible skew."),
    Mistake("No ND in daylight (no internal ND)", "Fix: variable ND filter; hold the 180° shutter angle."),
    Mistake("AWB shifting mid-shot", "Fix: lock manual white balance before enabling log; grey-card under the key light."),
    Mistake("Auto audio levels / built-in mic only", "Fix: manual levels, headphones, XLR handle for lav/boom; wind protection outdoors."),
    Mistake("Wrong codec for the job", "Fix: XAVC HS for efficient long-form, XAVC S-I for max quality at 60p and below.")
)

data class Spec(val label: String, val value: String)

val keySpecs: List<Spec> = listOf(
    Spec("Sensor", "26.1MP APS-C / Super 35 Exmor R BSI CMOS"),
    Spec("Processor", "BIONZ XR"),
    Spec("4K120 crop", "~1.6x additional crop at 119.88/100p"),
    Spec("Internal recording", "10-bit 4:2:2 up to 120p (XAVC HS / S)"),
    Spec("Codecs", "XAVC HS (H.265) · XAVC S (H.264) · XAVC S-I (All-Intra, 4K to 60p)"),
    Spec("Dual base ISO (S-Log3)", "800 / 2500"),
    Spec("Dual base ISO (S-Cinetone)", "125 / 400"),
    Spec("Dynamic range", "14+ stops in S-Log3"),
    Spec("Stabilization", "5-axis IBIS, 5.5 stops; Active mode adds ~1.13x crop"),
    Spec("Shutter angle", "Yes — firmware v5.00+ (5.6°–360°)"),
    Spec("Built-in waveform / false color", "No — use zebras + histogram, or the Sony Monitor & Control app / external monitor"),
    Spec("ND filter", "None internal — variable ND required"),
    Spec("Media", "Dual CFexpress Type A / SD UHS-II")
)
