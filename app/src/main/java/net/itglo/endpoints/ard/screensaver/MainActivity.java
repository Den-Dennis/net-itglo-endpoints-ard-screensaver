package net.itglo.endpoints.ard.screensaver;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.RestrictionsManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private String targetPackage = "";
    private String targetActivity = "";
    private boolean fallbackToHome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        loadConfig();
        buildUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadConfig();
        enterImmersiveMode();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) enterImmersiveMode();
    }

    private void loadConfig() {
        Bundle config = null;
        RestrictionsManager manager = (RestrictionsManager) getSystemService(Context.RESTRICTIONS_SERVICE);
        if (manager != null) config = manager.getApplicationRestrictions();
        targetPackage = getString(config, "target_package", "").trim();
        targetActivity = getString(config, "target_activity", "").trim();
        fallbackToHome = getBoolean(config, "fallback_to_home", true);
    }

    private void buildUi() {
        Bundle config = null;
        RestrictionsManager manager = (RestrictionsManager) getSystemService(Context.RESTRICTIONS_SERVICE);
        if (manager != null) config = manager.getApplicationRestrictions();
        String mainText = getString(config, "main_text", "TOUCH HERE");
        String subText = getString(config, "sub_text", "to start driver registration");

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.BLACK);
        root.setKeepScreenOn(true);
        root.setClickable(true);
        root.setFocusable(true);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) openTarget();
                return true;
            }
        });

        LinearLayout center = new LinearLayout(this);
        center.setOrientation(LinearLayout.VERTICAL);
        center.setGravity(Gravity.CENTER);
        center.setPadding(dp(24), dp(24), dp(24), dp(24));

        TextView main = new TextView(this);
        main.setText(mainText);
        main.setTextColor(Color.WHITE);
        main.setTextSize(42);
        main.setTypeface(Typeface.DEFAULT_BOLD);
        main.setGravity(Gravity.CENTER);
        main.setLetterSpacing(0.08f);

        TextView sub = new TextView(this);
        sub.setText(subText);
        sub.setTextColor(Color.rgb(190, 190, 190));
        sub.setTextSize(22);
        sub.setGravity(Gravity.CENTER);
        sub.setPadding(0, dp(12), 0, 0);

        View pulse = new View(this);
        LinearLayout.LayoutParams pulseParams = new LinearLayout.LayoutParams(dp(140), dp(4));
        pulseParams.setMargins(0, 0, 0, dp(28));
        pulse.setBackgroundColor(Color.rgb(215, 25, 32));
        pulse.setAlpha(0.45f);

        center.addView(pulse, pulseParams);
        center.addView(main, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        center.addView(sub, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        root.addView(center, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(root);

        ObjectAnimator fade = ObjectAnimator.ofFloat(center, View.ALPHA, 0.35f, 1.0f);
        fade.setDuration(1800);
        fade.setRepeatMode(ObjectAnimator.REVERSE);
        fade.setRepeatCount(ObjectAnimator.INFINITE);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(pulse, View.SCALE_X, 0.65f, 1.15f);
        scaleX.setDuration(1800);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(fade, scaleX);
        set.start();
    }

    private void openTarget() {
        Intent intent = null;
        if (!targetPackage.isEmpty() && !targetActivity.isEmpty()) {
            intent = new Intent();
            intent.setClassName(targetPackage, targetActivity);
        } else if (!targetPackage.isEmpty()) {
            PackageManager pm = getPackageManager();
            intent = pm.getLaunchIntentForPackage(targetPackage);
        }
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
                return;
            } catch (Exception ignored) {
                Toast.makeText(this, "Registration app not available", Toast.LENGTH_SHORT).show();
            }
        }
        if (fallbackToHome) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
        }
    }

    private void enterImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    private static String getString(Bundle bundle, String key, String fallback) {
        if (bundle == null || !bundle.containsKey(key)) return fallback;
        String value = bundle.getString(key);
        if (value == null || value.trim().isEmpty()) return fallback;
        return value;
    }

    private static boolean getBoolean(Bundle bundle, String key, boolean fallback) {
        if (bundle == null || !bundle.containsKey(key)) return fallback;
        return bundle.getBoolean(key, fallback);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
