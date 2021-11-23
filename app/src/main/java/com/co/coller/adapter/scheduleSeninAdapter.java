package com.co.coller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.co.coller.R;
import com.co.coller.model.schedule;

import java.util.ArrayList;

public class scheduleSeninAdapter extends RecyclerView.Adapter<scheduleSeninAdapter.ViewHolder> {

    private ArrayList<schedule> listSchedule;

    public scheduleSeninAdapter(ArrayList<schedule> listSchedule) {
        this.listSchedule = listSchedule;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_matkul, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtWaktu.setText(listSchedule.get(position).getWaktuMulai() + " - " + listSchedule.get(position).getWaktuBerakhir());
        holder.txtMatkul.setText(listSchedule.get(position).getNamaSchedule());

    }

    @Override
    public int getItemCount() {
        return listSchedule.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtWaktu, txtMatkul;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWaktu = itemView.findViewById(R.id.tvWaktu);
            txtMatkul = itemView.findViewById(R.id.tvMatkul);
        }
    }
}
