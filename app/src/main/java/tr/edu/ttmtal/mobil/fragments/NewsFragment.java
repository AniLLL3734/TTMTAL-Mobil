package tr.edu.ttmtal.mobil.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import tr.edu.ttmtal.mobil.R;
import tr.edu.ttmtal.mobil.adapters.NewsAdapter;
import tr.edu.ttmtal.mobil.models.NewsModel;

public class NewsFragment extends Fragment {

    private RecyclerView recyclerViewNews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewNews = view.findViewById(R.id.recyclerViewNews);
        
        List<NewsModel> newsList = new ArrayList<>();
        newsList.add(new NewsModel("1", "Tübitak 4006 Bilim Fuarı", "Okulumuzda yoğun katılımla TÜBİTAK bilim fuarı düzenlendi.", "https://picsum.photos/400/400?random=11", "24 Nis 2026", ""));
        newsList.add(new NewsModel("2", "Yeni Bilgisayar Laboratuvarı", "Bilişim bölümü öğrencileri için son teknoloji laboratuvar açıldı.", "https://picsum.photos/400/400?random=12", "20 Nis 2026", ""));
        newsList.add(new NewsModel("3", "Spor Müsabakalarında Şampiyonuz", "Voleybol takımımız okullar arası turnuvada il birincisi oldu.", "https://picsum.photos/400/400?random=13", "18 Nis 2026", ""));
        newsList.add(new NewsModel("8", "Mezuniyet Töreni Hazırlıkları", "Son sınıf öğrencileri için mezuniyet töreni hazırlıkları başladı.", "https://picsum.photos/400/400?random=14", "12 Nis 2026", ""));
        newsList.add(new NewsModel("9", "Erasmus+ Projesi Başladı", "Öğrencilerimiz Avrupa'da staj imkanı bulacak.", "https://picsum.photos/400/400?random=15", "05 Nis 2026", ""));

        NewsAdapter adapter = new NewsAdapter(newsList);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewNews.setAdapter(adapter);
    }
}
