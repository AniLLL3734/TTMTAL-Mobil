package tr.edu.ttmtal.mobil.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import tr.edu.ttmtal.mobil.R;
import tr.edu.ttmtal.mobil.models.CalendarEventModel;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.EventViewHolder> {

    private List<CalendarEventModel> events;

    public CalendarEventAdapter(List<CalendarEventModel> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        CalendarEventModel event = events.get(position);
        holder.tvTitle.setText(event.getTitle());
        holder.tvDesc.setText(event.getDescription() != null ? event.getDescription() : "");
        holder.tvDate.setText(event.getDate() != null ? event.getDate() : "");

        // Parse day and month from date string "YYYY-MM-DD"
        String date = event.getDate();
        if (date != null && date.length() >= 10) {
            try {
                String[] parts = date.split("-");
                holder.tvDay.setText(parts[2]);
                String[] months = {"OCA","ŞUB","MAR","NİS","MAY","HAZ","TEM","AĞU","EYL","EKİ","KAS","ARA"};
                int monthIdx = Integer.parseInt(parts[1]) - 1;
                if (monthIdx >= 0 && monthIdx < 12) {
                    holder.tvMonth.setText(months[monthIdx]);
                }
            } catch (Exception e) {
                holder.tvDay.setText("");
                holder.tvMonth.setText("");
            }
        }
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvDate, tvDay, tvMonth;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            tvDesc = itemView.findViewById(R.id.tvEventDesc);
            tvDate = itemView.findViewById(R.id.tvEventDate);
            tvDay = itemView.findViewById(R.id.tvEventDay);
            tvMonth = itemView.findViewById(R.id.tvEventMonth);
        }
    }
}
