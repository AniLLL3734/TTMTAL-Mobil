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
import tr.edu.ttmtal.mobil.models.NewsModel;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsModel> newsList;

    public NewsAdapter(List<NewsModel> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_card, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsModel news = newsList.get(position);
        holder.tvNewsTitle.setText(news.getTitle());
        holder.tvCategoryTag.setText(news.getDescription());
        holder.tvNewsDate.setText(news.getDate());
        String imageUrl = tr.edu.ttmtal.mobil.utils.ImageKitHelper.getUrl(news.getImageUrl());
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(android.R.color.darker_gray)
                .into(holder.ivNewsThumbnail);

        // Tıklanınca detay sayfasını aç
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), tr.edu.ttmtal.mobil.CommonDetailActivity.class);
            intent.putExtra("title", news.getTitle());
            intent.putExtra("category", news.getDescription());
            intent.putExtra("date", news.getDate());
            intent.putExtra("content", news.getContent());
            intent.putExtra("image_url", news.getImageUrl());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList != null ? newsList.size() : 0;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNewsThumbnail;
        TextView tvNewsTitle, tvCategoryTag, tvNewsDate;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNewsThumbnail = itemView.findViewById(R.id.ivNewsThumbnail);
            tvNewsTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvCategoryTag = itemView.findViewById(R.id.tvCategoryTag);
            tvNewsDate = itemView.findViewById(R.id.tvNewsDate);
        }
    }
}
