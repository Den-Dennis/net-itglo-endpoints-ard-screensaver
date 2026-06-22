#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TOOLS="/opt/data/.local/android-mvp-tools"
SDK="$TOOLS/android-sdk"
JDK="$TOOLS/jdk-17"
BUILD_TOOLS="$SDK/build-tools/35.0.0"
ANDROID_JAR="$SDK/platforms/android-35/android.jar"
BUILD_DIR="$ROOT/build/manual-android"
DIST_DIR="$ROOT/dist"
APP_NAME="driver-screensaver-v2026.0.1-debug"

export PATH="$JDK/bin:$BUILD_TOOLS:$SDK/cmdline-tools/latest/bin:$PATH"
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR/classes" "$BUILD_DIR/dex" "$DIST_DIR"

"$BUILD_TOOLS/aapt" package -f \
  -M "$ROOT/app/src/main/AndroidManifest.xml" \
  -S "$ROOT/app/src/main/res" \
  -I "$ANDROID_JAR" \
  -F "$BUILD_DIR/${APP_NAME}-unsigned.ap_" \
  --min-sdk-version 24 \
  --target-sdk-version 35

find "$ROOT/app/src/main/java" -name '*.java' | sort > "$BUILD_DIR/sources.txt"
"$JDK/bin/javac" -source 8 -target 8 \
  -bootclasspath "$ANDROID_JAR" \
  -classpath "$ANDROID_JAR" \
  -d "$BUILD_DIR/classes" \
  @"$BUILD_DIR/sources.txt"

"$BUILD_TOOLS/d8" \
  --lib "$ANDROID_JAR" \
  --output "$BUILD_DIR/dex" \
  $(find "$BUILD_DIR/classes" -name '*.class' | sort)

cp "$BUILD_DIR/${APP_NAME}-unsigned.ap_" "$BUILD_DIR/${APP_NAME}-unsigned.apk"
cd "$BUILD_DIR/dex"
"$BUILD_TOOLS/aapt" add "$BUILD_DIR/${APP_NAME}-unsigned.apk" classes.dex >/dev/null
cd "$ROOT"

KEYSTORE="$ROOT/keystore/driver-screensaver-debug.keystore"
mkdir -p "$ROOT/keystore"
if [ ! -f "$KEYSTORE" ]; then
  "$JDK/bin/keytool" -genkeypair -v \
    -keystore "$KEYSTORE" \
    -storepass android \
    -alias androiddebugkey \
    -keypass android \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -dname "CN=Driver Screensaver Debug,O=ITGLO,C=BE" >/dev/null
fi

"$BUILD_TOOLS/zipalign" -f -p 4 \
  "$BUILD_DIR/${APP_NAME}-unsigned.apk" \
  "$BUILD_DIR/${APP_NAME}-aligned.apk"

"$BUILD_TOOLS/apksigner" sign \
  --ks "$KEYSTORE" \
  --ks-key-alias androiddebugkey \
  --ks-pass pass:android \
  --key-pass pass:android \
  --out "$DIST_DIR/${APP_NAME}.apk" \
  "$BUILD_DIR/${APP_NAME}-aligned.apk"

"$BUILD_TOOLS/apksigner" verify --verbose --print-certs "$DIST_DIR/${APP_NAME}.apk" | tee "$BUILD_DIR/apksigner-verify.txt"
sha256sum "$DIST_DIR/${APP_NAME}.apk" | tee "$DIST_DIR/${APP_NAME}.apk.sha256"
"$BUILD_TOOLS/aapt" dump badging "$DIST_DIR/${APP_NAME}.apk" | tee "$BUILD_DIR/badging.txt"
"$BUILD_TOOLS/aapt" dump permissions "$DIST_DIR/${APP_NAME}.apk" | tee "$BUILD_DIR/permissions.txt"
"$BUILD_TOOLS/aapt" dump xmltree "$DIST_DIR/${APP_NAME}.apk" res/xml/app_restrictions.xml | tee "$BUILD_DIR/restrictions-xmltree.txt" >/dev/null
