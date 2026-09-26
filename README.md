# FX30 Companion

An Android field guide for the **Sony FX30** cinema camera: best settings for every
shooting scenario, plus interactive exposure scopes that teach you what correct
S-Log3 exposure *looks like*.

## Features

- **Shooting scenarios** — 6 full recipes (Cinematic 24p, Vlog, Low Light, Slow-mo 120p,
  Interview, Travel) with mode, codec, shutter, ISO, white balance, picture profile,
  audio, and stabilization.
- **Exposure Lab** — interactive waveform monitor + histogram driven by an exposure
  slider (−3 to +3 EV). Target bands show where skin (48–52% IRE), 18% grey (41%),
  white (61%), and clip (93%+) should sit in S-Log3.
- **Gamma curves** — S-Log3 vs Rec.709 comparison plot, IRE target table, ETTR explainer.
- **Reference** — all 12 Picture Profiles (PP1–PP11 + PPLUT), a 14-item mistakes
  checklist, zebra setup guide, false-color chart, 180° shutter presets, and key specs.

## Tech

- Kotlin + Jetpack Compose (Material 3), minSdk 26
- Custom `Canvas` scopes — no charting libraries
- JVM unit tests for the exposure math (`./gradlew testDebugUnitTest`)

## Build locally

Requires JDK 17 and the Android SDK (compileSdk 34):

```bash
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

## CI / GitHub

Every push to `main` runs unit tests and builds a debug APK via
[`.github/workflows/android.yml`](.github/workflows/android.yml); the APK is
uploaded as a build artifact for 30 days — install it straight from the
Actions tab to test on your phone.

### Publishing this repo to GitHub

```bash
cd fx30-companion
git init
git add .
git commit -m "FX30 Companion v1.0.0"
gh repo create fx30-companion --public --source=. --push
```

## Content sources

Settings and numbers were researched from Sony's official FX30 Help Guide
(picture profiles, bitrates, shutter-angle menu), Sony's cinematography articles
on exposing S-Log3, and independent guides (Alister Chapman, 4K Shooters,
CameraLAB, Keith Knittel, CineD, Thom Hogan). Key anchors used in the app:

| Reference | S-Log3 IRE |
|---|---|
| 18% middle grey | 41% |
| Average skin tones | 48–52% |
| 90% white card | ~61% |
| Hard clip | ~93–94% |

Dual base ISO: **800 / 2500** (S-Log3), **125 / 400** (S-Cinetone). The FX30 has
no built-in waveform monitor or false color — zebras (100+ clip / 70 skin),
histogram, and the Sony Monitor & Control app are the in-field tools.
