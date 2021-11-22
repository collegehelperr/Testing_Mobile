package com.co.coller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.co.coller.R;
import com.co.coller.model.note;

import java.util.ArrayList;

public class noteAdapter extends RecyclerView.Adapter<noteAdapter.ViewHolder> {

    private ArrayList<note> listNote;

    public noteAdapter(ArrayList<note> listNote) {
        this.listNote = listNote;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvJudul.setText(listNote.get(position).getJudulNote());
        holder.tvTanggal.setText(listNote.get(position).getTglNote());
        holder.tvBody.setText(listNote.get(position).getIsiNote());

    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvJudul, tvTanggal, tvBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJudul = itemView.findViewById(R.id.tv_judul_note);
            tvTanggal = itemView.findViewById(R.id.tv_tgl_note);
            tvBody = itemView.findViewById(R.id.tv_body_note);
        }
    }
}
