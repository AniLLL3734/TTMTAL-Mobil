package tr.edu.ttmtal.mobil.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CacheManager {
    private static final String PREF_NAME = "ttmtal_secure_cache";
    private SharedPreferences prefs;
    private Gson gson;

    public CacheManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            prefs = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to normal prefs if encryption fails
            prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        gson = new Gson();
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    // Versiyonları sakla/oku
    public void saveVersion(String node, int version) {
        prefs.edit().putInt("ver_" + node, version).apply();
    }

    public int getLocalVersion(String node) {
        return prefs.getInt("ver_" + node, 0);
    }

    // Genel JSON saklama/okuma
    public <T> void saveList(String key, List<T> list) {
        String json = gson.toJson(list);
        prefs.edit().putString(key, json).apply();
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        String json = prefs.getString(key, null);
        if (json == null) return new ArrayList<>();
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, type);
    }

    // Tekil obje saklama
    public <T> void saveObject(String key, T object) {
        String json = gson.toJson(object);
        prefs.edit().putString(key, json).apply();
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String json = prefs.getString(key, null);
        if (json == null) return null;
        return gson.fromJson(json, clazz);
    }
}
