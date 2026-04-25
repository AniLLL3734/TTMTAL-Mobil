package tr.edu.ttmtal.mobil.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import tr.edu.ttmtal.mobil.PhotoViewerActivity;
import tr.edu.ttmtal.mobil.R;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ImageViewHolder> {

    private List<String> imageUrls;

    public GalleryImageAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String rawUrl = imageUrls.get(position);
        String imageUrl = tr.edu.ttmtal.mobil.utils.ImageKitHelper.getUrl(rawUrl);
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(android.R.color.darker_gray)
                .into(holder.ivGalleryImage);

        holder.itemView.setOnClickListener(v -> {
            java.util.ArrayList<String> allUrls = new java.util.ArrayList<>();
            for (String s : imageUrls) {
                allUrls.add(tr.edu.ttmtal.mobil.utils.ImageKitHelper.getUrl(s));
            }
            Intent intent = new Intent(v.getContext(), tr.edu.ttmtal.mobil.PhotoViewerActivity.class);
            intent.putStringArrayListExtra("image_urls", allUrls);
            intent.putExtra("start_index", position);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGalleryImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGalleryImage = itemView.findViewById(R.id.ivGalleryImage);
        }
    }
}
