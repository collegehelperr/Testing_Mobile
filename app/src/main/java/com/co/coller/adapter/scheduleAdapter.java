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

public class scheduleAdapter extends RecyclerView.Adapter<scheduleAdapter.ViewHolder> {

    private ArrayList<schedule> listSchedule;
    private View.OnClickListener mOnItemClicklistener;

    public scheduleAdapter(ArrayList<schedule> listSchedule) {
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

        holder.txtWaktuMulai.setText(listSchedule.get(position).getWaktuMulai());
        holder.txtWaktuBerakhir.setText(listSchedule.get(position).getWaktuBerakhir());
        holder.txtMatkul.setText(listSchedule.get(position).getNamaSchedule());

    }

    @Override
    public int getItemCount() {
        return listSchedule.size();
    }

    public void setOnItemClicklistener(View.OnClickListener itemClicklistener){
        mOnItemClicklistener = itemClicklistener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtWaktuMulai, txtWaktuBerakhir, txtMatkul;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWaktuMulai = itemView.findViewById(R.id.tvWaktuMulai);
            txtWaktuBerakhir = itemView.findViewById(R.id.tvWaktuBerakhir);
            txtMatkul = itemView.findViewById(R.id.tvMatkul);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClicklistener);
        }
    }
}
