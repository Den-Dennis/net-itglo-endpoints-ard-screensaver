# Driver Screensaver Project

## Goal

Provide a small Android APK that acts as a black attract screen for truck driver self-registration devices. Drivers see a clear visual cue, touch the screen, and are forwarded into the real registration flow.

## Scope v2026.0.1

- Native Java Android app; no Gradle dependency required.
- Package: `net.itglo.driver.screensaver`.
- Fullscreen/immersive landscape attract screen.
- Managed configuration for text and target registration app.
- No network or sensitive permissions.
- Debug APK built and statically verified with Android SDK tooling.

## Deferred

- Idle timeout that relaunches the screensaver after inactivity in the registration app.
- Device-owner/kiosk lock-task integration.
- Brand/logo assets.
- Multilingual text variants.
