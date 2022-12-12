package com.example.book2run.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book2run.R;
import com.example.book2run.ui.notifications.NotificationsFragment;

public class RecyclerViewCircuit extends RecyclerView.Adapter<RecyclerViewCircuit.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }

    private OnItemClickListener itemClickListener;

    public RecyclerViewCircuit(int[] data) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptercircuit_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                if(itemClickListener != null) itemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickListener.onItemLongClick(v, holder.getAdapterPosition());
                return false;
            }
        });
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {this.itemClickListener = itemClickListener;}

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.image_recycler);
        }
    }
}
