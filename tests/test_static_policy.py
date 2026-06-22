import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MANIFEST = (ROOT / "app/src/main/AndroidManifest.xml").read_text()
MAIN = (ROOT / "app/src/main/java/net/itglo/endpoints/ard/screensaver/MainActivity.java").read_text()


class StaticPolicyTest(unittest.TestCase):
    def test_package_and_version(self):
        self.assertIn('package="net.itglo.endpoints.ard.screensaver"', MANIFEST)
        self.assertIn('android:versionName="2026.0.1"', MANIFEST)

    def test_no_network_or_sensitive_permissions(self):
        forbidden = ["INTERNET", "READ_CONTACTS", "WRITE_CONTACTS", "GET_ACCOUNTS", "READ_PHONE_STATE"]
        for permission in forbidden:
            self.assertNotIn(permission, MANIFEST)

    def test_touch_launch_behavior_exists(self):
        self.assertIn("openTarget()", MAIN)
        self.assertIn("getLaunchIntentForPackage", MAIN)
        self.assertIn("target_package", MAIN)

    def test_immersive_black_attract_screen(self):
        self.assertIn("Color.BLACK", MAIN)
        self.assertIn("SYSTEM_UI_FLAG_IMMERSIVE_STICKY", MAIN)
        self.assertIn("FLAG_KEEP_SCREEN_ON", MAIN)


if __name__ == "__main__":
    unittest.main()
