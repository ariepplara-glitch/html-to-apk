package com.agunpu.game;

import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.JavascriptInterface;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // default portrait (game tanpa meta orientation = portrait, misal Neon Dodge)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        // jembatan JS -> native: tombol KELUAR + orientasi otomatis per-game
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void exitApp() {
                runOnUiThread(new Runnable() {
                    @Override public void run() { finish(); }
                });
            }
            @JavascriptInterface
            public void setOrientation(String mode) {
                final int o;
                if ("landscape".equals(mode)) o = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                else if ("portrait".equals(mode)) o = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                else o = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
                runOnUiThread(new Runnable() {
                    @Override public void run() { setRequestedOrientation(o); }
                });
            }
        }, "Android");

        webView.loadUrl("file:///android_asset/www/index.html");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
