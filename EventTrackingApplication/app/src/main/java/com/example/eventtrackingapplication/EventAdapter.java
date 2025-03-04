package com.example.eventtrackingapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events;
    private OnEventActionListener listener;

    public interface OnEventActionListener {
        void onEditEvent(int position); // Callback for editing
        void onDeleteEvent(int position); // Callback for deleting
    }

    public EventAdapter(List<Event> events, OnEventActionListener listener) {
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventName.setText(event.getName());
        holder.eventTime.setText(event.getTime());

        // Handle edit event (click on the item itself)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditEvent(holder.getAdapterPosition());
            }
        });

        // Handle delete event (click on the delete button)
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteEvent(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventTime;
        Button deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventTime = itemView.findViewById(R.id.eventTime);
            deleteButton = itemView.findViewById(R.id.deleteButton); // Ensure this matches the ID in your layout
        }
    }
}
