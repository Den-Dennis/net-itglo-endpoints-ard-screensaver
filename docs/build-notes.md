# Build notes

## v2026.0.2

Initial debug APK.

- Package: `net.itglo.endpoints.ard.screensaver`
- APK: `dist/ard-screensaver-v2026.0.2-debug.apk`
- SHA256: `05204234141253717414cc2e233f1d7f4879d720f5cb4182fb94368d574d170f`
- Permissions: none declared
- Verified with:
  - `scripts/build-debug-apk.sh`
  - `python -m unittest discover -s tests`
  - `aapt dump badging`
  - `aapt dump permissions`
  - `apksigner verify --verbose --print-certs`
