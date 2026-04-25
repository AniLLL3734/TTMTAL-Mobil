package tr.edu.ttmtal.mobil.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import tr.edu.ttmtal.mobil.R;
import tr.edu.ttmtal.mobil.models.NotesModel;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<NotesModel> notesList;

    public NotesAdapter(List<NotesModel> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        NotesModel note = notesList.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.tvDate.setText(note.getDate());
    }

    @Override
    public int getItemCount() {
        return notesList != null ? notesList.size() : 0;
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvContent = itemView.findViewById(R.id.tvNoteContent);
            tvDate = itemView.findViewById(R.id.tvNoteDate);
        }
    }
}
