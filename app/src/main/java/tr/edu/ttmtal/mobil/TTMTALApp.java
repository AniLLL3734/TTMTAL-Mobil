package tr.edu.ttmtal.mobil;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

import tr.edu.ttmtal.mobil.utils.ImageKitHelper;

public class TTMTALApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize dynamic config from cache if available, otherwise from resources
        SharedPreferences cachePrefs = getSharedPreferences("ttmtal_secure_cache", MODE_PRIVATE);
        ImageKitHelper.ENDPOINT = cachePrefs.getString("imagekit_url_endpoint", getString(R.string.imagekit_url_endpoint));

        SharedPreferences prefs = getSharedPreferences("ttmtal_prefs", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
