package tr.edu.ttmtal.mobil.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import java.util.Calendar;
import tr.edu.ttmtal.mobil.ContactActivity;
import tr.edu.ttmtal.mobil.R;
import tr.edu.ttmtal.mobil.adapters.NewsAdapter;
import tr.edu.ttmtal.mobil.adapters.SliderAdapter;
import tr.edu.ttmtal.mobil.models.MockDataUtil;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Dynamic greeting based on time
        TextView tvGreeting = view.findViewById(R.id.tvGreetingText);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 12) {
            tvGreeting.setText("Günaydın,");
        } else if (hour >= 12 && hour < 18) {
            tvGreeting.setText("İyi Günler,");
        } else if (hour >= 18 && hour < 22) {
            tvGreeting.setText("İyi Akşamlar,");
        } else {
            tvGreeting.setText("İyi Geceler,");
        }

        tr.edu.ttmtal.mobil.utils.CacheManager cache = new tr.edu.ttmtal.mobil.utils.CacheManager(requireContext());

        // Slider - Pick 3 random news
        ViewPager2 viewPager = view.findViewById(R.id.viewPagerSlider);
        java.util.List<tr.edu.ttmtal.mobil.models.NewsModel> allPossibleNews = cache.getList("news", tr.edu.ttmtal.mobil.models.NewsModel.class);
        if (allPossibleNews == null || allPossibleNews.isEmpty()) allPossibleNews = tr.edu.ttmtal.mobil.models.MockDataUtil.getPopularNews();
        
        java.util.List<tr.edu.ttmtal.mobil.models.NewsModel> sliderData = new java.util.ArrayList<>(allPossibleNews);
        java.util.Collections.shuffle(sliderData);
        if (sliderData.size() > 3) sliderData = sliderData.subList(0, 3);
        viewPager.setAdapter(new tr.edu.ttmtal.mobil.adapters.SliderAdapter(sliderData));

        // News list
        RecyclerView rv = view.findViewById(R.id.recyclerViewNews);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        java.util.List<tr.edu.ttmtal.mobil.models.NewsModel> newsData = cache.getList("news", tr.edu.ttmtal.mobil.models.NewsModel.class);
        if (newsData == null || newsData.isEmpty()) newsData = tr.edu.ttmtal.mobil.models.MockDataUtil.getPopularNews();
        
        final java.util.List<tr.edu.ttmtal.mobil.models.NewsModel> allNews;
        if (getArguments() != null && getArguments().containsKey("category_filter")) {
            String filter = getArguments().getString("category_filter").toLowerCase();
            allNews = new java.util.ArrayList<>();
            for (tr.edu.ttmtal.mobil.models.NewsModel n : newsData) {
                if (n.getDescription() != null && n.getDescription().toLowerCase().contains(filter)) {
                    allNews.add(n);
                }
            }
            // Update title or something if needed? For now just filter.
        } else {
            allNews = newsData;
        }
        rv.setAdapter(new tr.edu.ttmtal.mobil.adapters.NewsAdapter(allNews));

        // Quick Actions
        view.findViewById(R.id.actionProgram).setOnClickListener(v -> {
            if (getActivity() != null) {
                ((tr.edu.ttmtal.mobil.MainActivity) getActivity()).switchToFragment(new ProgramsFragment());
            }
        });

        view.findViewById(R.id.actionNotlar).setOnClickListener(v -> {
            if (getActivity() != null) {
                ((tr.edu.ttmtal.mobil.MainActivity) getActivity()).switchToFragment(new NotesFragment());
            }
        });

        view.findViewById(R.id.actionIletisim).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ContactActivity.class));
        });
    }
}
