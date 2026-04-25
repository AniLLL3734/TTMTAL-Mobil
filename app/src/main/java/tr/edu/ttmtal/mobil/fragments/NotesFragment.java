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
import tr.edu.ttmtal.mobil.adapters.NotesAdapter;
import tr.edu.ttmtal.mobil.models.NotesModel;
import tr.edu.ttmtal.mobil.utils.CacheManager;

public class NotesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        RecyclerView rv = view.findViewById(R.id.rvNotes);
        View layoutEmpty = view.findViewById(R.id.layoutEmpty);
        ProgressBar progress = view.findViewById(R.id.progressNotes);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Show cache first
        CacheManager cache = new CacheManager(requireContext());
        List<NotesModel> cachedNotes = cache.getList("notes", NotesModel.class);
        if (!cachedNotes.isEmpty()) {
            rv.setAdapter(new NotesAdapter(cachedNotes));
            layoutEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }

        // Fetch from Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance("https://ttmtal-mobil-95160-default-rtdb.europe-west1.firebasedatabase.app").getReference("notes");
        if (progress != null) progress.setVisibility(View.VISIBLE);
        
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded()) return;
                if (progress != null) progress.setVisibility(View.GONE);
                
                List<NotesModel> notes = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    NotesModel item = child.getValue(NotesModel.class);
                    if (item != null) notes.add(item);
                }
                
                cache.saveList("notes", notes);
                updateUI(rv, layoutEmpty, notes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (!isAdded()) return;
                if (progress != null) progress.setVisibility(View.GONE);
                // Keep showing cache
            }
        });

        return view;
    }

    private void updateUI(RecyclerView rv, View layoutEmpty, List<NotesModel> notes) {
        if (notes.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setAdapter(new NotesAdapter(notes));
        }
    }
}
