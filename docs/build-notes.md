# Build notes

## v2026.0.1

Initial debug APK.

- Package: `net.itglo.driver.screensaver`
- APK: `dist/driver-screensaver-v2026.0.1-debug.apk`
- SHA256: `5e8cdedecc8c5b0d4151776aba31f744195f57130ea20fc895e5d3b1d9442e1c`
- Permissions: none declared
- Verified with:
  - `scripts/build-debug-apk.sh`
  - `python -m unittest discover -s tests`
  - `aapt dump badging`
  - `aapt dump permissions`
  - `apksigner verify --verbose --print-certs`
