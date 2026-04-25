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
import tr.edu.ttmtal.mobil.models.CategoryModel;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryModel> categoryList;

    public CategoryAdapter(List<CategoryModel> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_card, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());
        String imageUrl = tr.edu.ttmtal.mobil.utils.ImageKitHelper.getUrl(category.getImageUrl());
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(android.R.color.darker_gray)
                .into(holder.ivCategoryBg);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), tr.edu.ttmtal.mobil.CommonDetailActivity.class);
            intent.putExtra("title", category.getName());
            intent.putExtra("category", "Kategori");
            intent.putExtra("content", category.getContent());
            intent.putExtra("image_url", category.getImageUrl());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryBg;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryBg = itemView.findViewById(R.id.ivCategoryBg);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
