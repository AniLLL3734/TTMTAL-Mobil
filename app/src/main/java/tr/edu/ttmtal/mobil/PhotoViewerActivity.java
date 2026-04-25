package tr.edu.ttmtal.mobil;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import tr.edu.ttmtal.mobil.adapters.PhotoViewerAdapter;

public class PhotoViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        ArrayList<String> imageUrls = getIntent().getStringArrayListExtra("image_urls");
        int startIndex = getIntent().getIntExtra("start_index", 0);

        ViewPager2 viewPager = findViewById(R.id.viewPagerPhoto);
        ImageView ivClose = findViewById(R.id.ivClose);

        if (imageUrls != null && !imageUrls.isEmpty()) {
            PhotoViewerAdapter adapter = new PhotoViewerAdapter(imageUrls);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(startIndex, false);
        }

        ivClose.setOnClickListener(v -> finish());
    }
}
