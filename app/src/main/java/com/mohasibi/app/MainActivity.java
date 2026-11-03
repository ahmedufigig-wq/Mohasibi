package com.mohasibi.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import android.util.Base64;
import android.view.View;
import android.graphics.Color;

public class MainActivity extends Activity {
    private WebView web;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(12, 20, 36));
        getWindow().setNavigationBarColor(Color.rgb(12, 20, 36));
        getWindow().getDecorView().setSystemUiVisibility(0);
        web = new WebView(this);
        web.setBackgroundColor(Color.rgb(9, 16, 29));
        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(new SecurityBridge(), "MohasibiSecurity");
        web.addJavascriptInterface(new SecurityBridge(), "MohasibiSecurity");
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());
        setContentView(web);
        if (savedInstanceState == null) web.loadUrl("file:///android_asset/index.html");
        else web.restoreState(savedInstanceState);
    }
    public class SecurityBridge {
        @JavascriptInterface public String newSalt() {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            return Base64.encodeToString(salt, Base64.NO_WRAP);
        }
        @JavascriptInterface public String hashPassword(String password, String saltText) {
            try {
                byte[] salt = Base64.decode(saltText, Base64.DEFAULT);
                PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 120000, 256);
                byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
                spec.clearPassword();
                return Base64.encodeToString(hash, Base64.NO_WRAP);
            } catch (Exception e) { return ""; }
        }
    }
    public class SecurityBridge {
        @JavascriptInterface public String newSalt() {
            byte[] salt = new byte[16]; new SecureRandom().nextBytes(salt);
            return Base64.encodeToString(salt, Base64.NO_WRAP);
        }
        @JavascriptInterface public String hashPassword(String password, String saltText) {
            try {
                byte[] salt = Base64.decode(saltText, Base64.DEFAULT);
                PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 120000, 256);
                byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
                spec.clearPassword(); return Base64.encodeToString(hash, Base64.NO_WRAP);
            } catch (Exception e) { return ""; }
        }
        @JavascriptInterface public void exitApp() { runOnUiThread(() -> finish()); }
    }
    @Override protected void onSaveInstanceState(Bundle outState) { super.onSaveInstanceState(outState); web.saveState(outState); }
    @Override public void onBackPressed() {
        if (web != null && web.canGoBack()) web.goBack(); else super.onBackPressed();
    }
}
