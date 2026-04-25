package tr.edu.ttmtal.mobil;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;

public class CommonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        ImageView ivImage = findViewById(R.id.ivDetailImage);
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDate = findViewById(R.id.tvDetailDate);
        TextView tvContent = findViewById(R.id.tvDetailContent);

        String title = getIntent().getStringExtra("title");
        String category = getIntent().getStringExtra("category");
        String date = getIntent().getStringExtra("date");
        String content = getIntent().getStringExtra("content");
        String imageUrl = getIntent().getStringExtra("image_url");

        tvTitle.setText(title);
        tvCategory.setText(category != null ? category : "Genel");
        tvDate.setText(date != null ? date : "");
        tvContent.setText(content != null ? content : "İçerik bulunmamaktadır.");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            String fullUrl = tr.edu.ttmtal.mobil.utils.ImageKitHelper.getUrl(imageUrl);
            Glide.with(this).load(fullUrl).into(ivImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
