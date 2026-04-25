package tr.edu.ttmtal.mobil;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import tr.edu.ttmtal.mobil.adapters.GalleryAdapter;
import tr.edu.ttmtal.mobil.models.GalleryModel;
import tr.edu.ttmtal.mobil.utils.CacheManager;

public class GalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = findViewById(R.id.toolbarGallery);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.recyclerViewGallery);
        rv.setLayoutManager(new LinearLayoutManager(this));

        CacheManager cache = new CacheManager(this);

        // Try Firebase first, fallback to cache
        DatabaseReference ref = FirebaseDatabase.getInstance("https://ttmtal-mobil-95160-default-rtdb.europe-west1.firebasedatabase.app").getReference("gallery");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                List<GalleryModel> galleryData = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    GalleryModel model = child.getValue(GalleryModel.class);
                    if (model != null) galleryData.add(model);
                }
                if (!galleryData.isEmpty()) {
                    cache.saveList("gallery", galleryData);
                    rv.setAdapter(new GalleryAdapter(galleryData));
                } else {
                    // Fallback to cache
                    List<GalleryModel> cached = cache.getList("gallery", GalleryModel.class);
                    rv.setAdapter(new GalleryAdapter(cached));
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Fallback to cache
                List<GalleryModel> cached = cache.getList("gallery", GalleryModel.class);
                rv.setAdapter(new GalleryAdapter(cached));
            }
        });
    }
}
