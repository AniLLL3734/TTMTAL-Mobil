package tr.edu.ttmtal.mobil.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import tr.edu.ttmtal.mobil.PhotoViewerActivity;
import tr.edu.ttmtal.mobil.R;
import tr.edu.ttmtal.mobil.models.GalleryModel;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<GalleryModel> galleryList;

    public GalleryAdapter(List<GalleryModel> galleryList) {
        this.galleryList = galleryList;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        GalleryModel item = galleryList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvAuthor.setText(item.getAuthorName());
        holder.tvDate.setText(item.getDate());

        List<String> urls = item.getImageUrls();
        GalleryImageAdapter adapter = new GalleryImageAdapter(urls);
        holder.rvImages.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(holder.itemView.getContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
        holder.rvImages.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return galleryList != null ? galleryList.size() : 0;
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvDate;
        RecyclerView rvImages;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGalleryTitle);
            tvAuthor = itemView.findViewById(R.id.tvGalleryAuthor);
            tvDate = itemView.findViewById(R.id.tvGalleryDate);
            rvImages = itemView.findViewById(R.id.rvGalleryImages);
        }
    }
}
