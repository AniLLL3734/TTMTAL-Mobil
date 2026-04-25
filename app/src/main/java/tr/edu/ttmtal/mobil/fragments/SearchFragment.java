package tr.edu.ttmtal.mobil.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import tr.edu.ttmtal.mobil.R;
import android.widget.ProgressBar;
import tr.edu.ttmtal.mobil.adapters.CategoryAdapter;
import tr.edu.ttmtal.mobil.models.MockDataUtil;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerViewCategories;
    private java.util.List<tr.edu.ttmtal.mobil.models.CategoryModel> categories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tr.edu.ttmtal.mobil.utils.CacheManager cache = new tr.edu.ttmtal.mobil.utils.CacheManager(requireContext());
        categories = cache.getList("categories", tr.edu.ttmtal.mobil.models.CategoryModel.class);
        if (categories == null || categories.isEmpty()) categories = tr.edu.ttmtal.mobil.models.MockDataUtil.getCategories();

        ProgressBar progress = view.findViewById(R.id.progressSearch);
        if (progress != null) progress.setVisibility(View.GONE);

        // Search Logic
        android.widget.EditText etSearch = view.findViewById(R.id.etSearch);
        if (etSearch != null) {
            etSearch.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String query = s.toString().toLowerCase();
                    java.util.List<tr.edu.ttmtal.mobil.models.CategoryModel> filtered = new java.util.ArrayList<>();
                    for (tr.edu.ttmtal.mobil.models.CategoryModel c : categories) {
                        if (c.getName().toLowerCase().contains(query)) filtered.add(c);
                    }
                    recyclerViewCategories.setAdapter(new CategoryAdapter(filtered));
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });
        }

        if (recyclerViewCategories != null) {
            recyclerViewCategories.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            recyclerViewCategories.setAdapter(new CategoryAdapter(categories));
        }
    }
}
