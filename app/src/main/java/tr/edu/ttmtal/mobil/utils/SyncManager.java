package tr.edu.ttmtal.mobil.utils;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import tr.edu.ttmtal.mobil.models.CalendarEventModel;
import tr.edu.ttmtal.mobil.models.GalleryModel;
import tr.edu.ttmtal.mobil.models.NewsModel;
import tr.edu.ttmtal.mobil.models.CategoryModel;
import tr.edu.ttmtal.mobil.models.NotesModel;

public class SyncManager {
    private static final String TAG = "SyncManager";
    private DatabaseReference mDatabase;
    private CacheManager cache;
    private SyncCallback callback;

    public interface SyncCallback {
        void onSyncComplete();
    }

    public SyncManager(Context context, SyncCallback callback) {
        this.mDatabase = FirebaseDatabase.getInstance("https://ttmtal-mobil-95160-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        this.cache = new CacheManager(context);
        this.callback = callback;
    }

    public void startSync() {
        Log.d(TAG, "Sync started...");
        fetchRemoteConfig(); // Öncelikle anahtarları çek
    }

    private void fetchRemoteConfig() {
        mDatabase.child("remote_config").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    android.content.SharedPreferences prefs = cache.getPrefs();
                    android.content.SharedPreferences.Editor editor = prefs.edit();
                    
                    if (snapshot.hasChild("groq_api_key")) 
                        editor.putString("groq_api_key", snapshot.child("groq_api_key").getValue(String.class));
                    
                    if (snapshot.hasChild("imagekit_public_key"))
                        editor.putString("imagekit_public_key", snapshot.child("imagekit_public_key").getValue(String.class));
                        
                    if (snapshot.hasChild("imagekit_url_endpoint"))
                        editor.putString("imagekit_url_endpoint", snapshot.child("imagekit_url_endpoint").getValue(String.class));
                    
                    editor.apply();
                    Log.d(TAG, "Remote config fetched successfully.");
                    
                    // Anahtarlar güncellendiği için ImageKitHelper'ı da güncelle
                    ImageKitHelper.ENDPOINT = prefs.getString("imagekit_url_endpoint", "");
                }
                
                // Anahtarları çektikten sonra normal senkronizasyona devam et
                continueSync();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Remote config fetch failed: " + error.getMessage());
                continueSync();
            }
        });
    }

    private void continueSync() {
        mDatabase.child("app_version").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists() && snapshot.getValue() != null) {
                        Object value = snapshot.getValue();
                        if (value instanceof Map) {
                            Map<String, Long> remoteVersions = (Map<String, Long>) value;
                            checkAndSync(remoteVersions);
                        } else {
                            Log.e(TAG, "app_version is not a Map: " + value.getClass().getName());
                            if (callback != null) callback.onSyncComplete();
                        }
                    } else {
                        Log.d(TAG, "app_version node not found on Firebase. Using local cache.");
                        if (callback != null) callback.onSyncComplete();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error in startSync onDataChange: " + e.getMessage());
                    if (callback != null) callback.onSyncComplete();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Version fetch failed: " + error.getMessage());
                if (callback != null) callback.onSyncComplete();
            }
        });
    }

    private void checkAndSync(Map<String, Long> remoteVersions) {
        // Kontrol edilecek nodelar
        String[] nodes = {"news", "gallery", "slider", "categories", "notes", "calendar"};
        final int[] nodesToSync = {0};

        for (String node : nodes) {
            int localVer = cache.getLocalVersion(node);
            long remoteVer = remoteVersions.getOrDefault(node, 0L);

            if (remoteVer > localVer) {
                nodesToSync[0]++;
                syncNode(node, (int) remoteVer, () -> {
                    nodesToSync[0]--;
                    if (nodesToSync[0] == 0) {
                        if (callback != null) callback.onSyncComplete();
                    }
                });
            }
        }

        if (nodesToSync[0] == 0) {
            Log.d(TAG, "All nodes are up to date.");
            if (callback != null) callback.onSyncComplete();
        }
    }

    private void syncNode(String node, int newVersion, Runnable onDone) {
        Log.d(TAG, "Syncing node: " + node + " (New Version: " + newVersion + ")");
        mDatabase.child(node).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (node.equals("news") || node.equals("slider")) {
                        List<NewsModel> list = new ArrayList<>();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            NewsModel item = postSnapshot.getValue(NewsModel.class);
                            if (item != null) list.add(item);
                        }
                        cache.saveList(node, list);
                    } else if (node.equals("gallery")) {
                        List<GalleryModel> list = new ArrayList<>();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            GalleryModel item = postSnapshot.getValue(GalleryModel.class);
                            if (item != null) list.add(item);
                        }
                        cache.saveList(node, list);
                    } else if (node.equals("categories")) {
                        List<CategoryModel> list = new ArrayList<>();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            CategoryModel item = postSnapshot.getValue(CategoryModel.class);
                            if (item != null) list.add(item);
                        }
                        cache.saveList(node, list);
                    } else if (node.equals("notes")) {
                        List<NotesModel> list = new ArrayList<>();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            NotesModel item = postSnapshot.getValue(NotesModel.class);
                            if (item != null) list.add(item);
                        }
                        cache.saveList(node, list);
                    } else if (node.equals("calendar")) {
                        List<CalendarEventModel> list = new ArrayList<>();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            CalendarEventModel item = postSnapshot.getValue(CalendarEventModel.class);
                            if (item != null) list.add(item);
                        }
                        cache.saveList(node, list);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error syncing " + node + ": " + e.getMessage());
                }

                // Versiyonu güncelle
                cache.saveVersion(node, newVersion);
                onDone.run();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Sync failed for " + node + ": " + error.getMessage());
                onDone.run();
            }
        });
    }
}
