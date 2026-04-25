package tr.edu.ttmtal.mobil.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import tr.edu.ttmtal.mobil.R;
import tr.edu.ttmtal.mobil.adapters.CalendarEventAdapter;
import tr.edu.ttmtal.mobil.models.CalendarEventModel;
import tr.edu.ttmtal.mobil.utils.CacheManager;

public class CalendarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        RecyclerView rv = view.findViewById(R.id.rvCalendar);
        View layoutEmpty = view.findViewById(R.id.layoutCalendarEmpty);
        ProgressBar progress = view.findViewById(R.id.progressCalendar);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Load from Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance("https://ttmtal-mobil-95160-default-rtdb.europe-west1.firebasedatabase.app").getReference("calendar");
        progress.setVisibility(View.VISIBLE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded()) return;
                progress.setVisibility(View.GONE);
                List<CalendarEventModel> events = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    CalendarEventModel event = child.getValue(CalendarEventModel.class);
                    if (event != null) events.add(event);
                }
                // Also save to cache
                new CacheManager(requireContext()).saveList("calendar", events);
                showData(rv, layoutEmpty, events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (!isAdded()) return;
                progress.setVisibility(View.GONE);
                // Fallback to cache
                List<CalendarEventModel> cached = new CacheManager(requireContext()).getList("calendar", CalendarEventModel.class);
                showData(rv, layoutEmpty, cached);
            }
        });

        return view;
    }

    private void showData(RecyclerView rv, View layoutEmpty, List<CalendarEventModel> events) {
        if (events.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setAdapter(new CalendarEventAdapter(events));
        }
    }
}
