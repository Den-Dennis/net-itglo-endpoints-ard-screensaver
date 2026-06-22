# net-itglo-driver-screensaver

Minimal Android attract/screen-saver app for truck driver self-registration kiosks.

## Current artifact

- APK: `dist/driver-screensaver-v2026.0.1-debug.apk`
- Package: `net.itglo.driver.screensaver`
- Version: `2026.0.1` / versionCode `1`
- SHA256: `5e8cdedecc8c5b0d4151776aba31f744195f57130ea20fc895e5d3b1d9442e1c`

## What this build does

- Shows a fullscreen black screen in landscape.
- Keeps the screen awake while the attract screen is visible.
- Hides Android system bars using immersive sticky mode.
- Shows a subtle pulsing visual indication: **TOUCH HERE** / `to start driver registration`.
- On touch, opens the configured registration app package/activity.
- If no target app is configured, it falls back to Android Home by default.
- Exposes managed configuration fields for the on-screen text and target app.

## Managed configuration

| Key | Type | Default | Purpose |
| --- | --- | --- | --- |
| `main_text` | string | `TOUCH HERE` | Main attract text |
| `sub_text` | string | `to start driver registration` | Smaller helper text |
| `target_package` | string | empty | Registration app package to launch after touch |
| `target_activity` | string | empty | Optional fully qualified Activity class |
| `fallback_to_home` | bool | `true` | Go to Android Home when target app is missing |

Example MDM values:

```json
{
  "main_text": "TOUCH HERE",
  "sub_text": "to start driver registration",
  "target_package": "com.company.registration",
  "target_activity": "",
  "fallback_to_home": true
}
```

## Build

```bash
scripts/build-debug-apk.sh
```

The build uses the local Android SDK/JDK toolchain at `/opt/data/.local/android-mvp-tools`.

## Verification

```bash
python -m unittest discover -s tests
/opt/data/.local/android-mvp-tools/android-sdk/build-tools/35.0.0/aapt dump badging dist/driver-screensaver-v2026.0.1-debug.apk
/opt/data/.local/android-mvp-tools/android-sdk/build-tools/35.0.0/aapt dump permissions dist/driver-screensaver-v2026.0.1-debug.apk
/opt/data/.local/android-mvp-tools/android-sdk/build-tools/35.0.0/apksigner verify --verbose --print-certs dist/driver-screensaver-v2026.0.1-debug.apk
```

## Real-device validation still needed

Install on the kiosk tablet, set the target registration package via MDM, open the app, touch the screen, and confirm the registration app starts.
