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
import tr.edu.ttmtal.mobil.R;
import tr.edu.ttmtal.mobil.models.NewsModel;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<NewsModel> newsList;

    public SliderAdapter(List<NewsModel> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        NewsModel news = newsList.get(position);
        holder.tvSliderTitle.setText(news.getTitle());
        holder.tvSliderDate.setText(news.getDate());
        String imageUrl = tr.edu.ttmtal.mobil.utils.ImageKitHelper.getUrl(news.getImageUrl());
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(android.R.color.darker_gray)
                .into(holder.ivSliderImage);

        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(v.getContext(), tr.edu.ttmtal.mobil.CommonDetailActivity.class);
            intent.putExtra("title", news.getTitle());
            intent.putExtra("category", "Öne Çıkan");
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

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSliderImage;
        TextView tvSliderTitle;
        TextView tvSliderDate;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSliderImage = itemView.findViewById(R.id.ivSliderImage);
            tvSliderTitle = itemView.findViewById(R.id.tvSliderTitle);
            tvSliderDate = itemView.findViewById(R.id.tvSliderDate);
        }
    }
}
