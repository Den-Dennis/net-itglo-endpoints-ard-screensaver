# Managed configuration

The APK exposes Android Enterprise app restrictions so SOTI/MobiControl or another MDM can configure the attract text and the registration app to open after touch.

| Key | Type | Default | Notes |
| --- | --- | --- | --- |
| `main_text` | string | `TOUCH HERE` | Main centered text |
| `sub_text` | string | `to start driver registration` | Smaller helper text |
| `target_package` | string | empty | Package name of the registration app |
| `target_activity` | string | empty | Optional full Activity class; leave empty for launcher activity |
| `fallback_to_home` | bool | `true` | Opens Android Home if no target package/activity can be launched |

Recommended first deployment: configure only `target_package` and leave `target_activity` empty. Android will open the target app's launcher activity.
