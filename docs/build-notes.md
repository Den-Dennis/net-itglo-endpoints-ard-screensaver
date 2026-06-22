# Build notes

## v2026.0.1

Initial debug APK.

- Package: `net.itglo.endpoints.ard.screensaver`
- APK: `dist/ard-screensaver-v2026.0.1-debug.apk`
- SHA256: `8feffa72e5470ac5ba808c6ce25fdb8b7a8351108eb5ec262220ad89bc6bc896`
- Permissions: none declared
- Verified with:
  - `scripts/build-debug-apk.sh`
  - `python -m unittest discover -s tests`
  - `aapt dump badging`
  - `aapt dump permissions`
  - `apksigner verify --verbose --print-certs`
