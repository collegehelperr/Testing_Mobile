package com.co.coller.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.co.coller.R;

import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.co.coller.model.task;
import com.google.gson.JsonObject;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class taskAdapter extends RecyclerView.Adapter<taskAdapter.ViewHolder> {

    ArrayList<task> listTask;
    private View.OnClickListener mOnItemClicklistener;

    public taskAdapter(ArrayList<task> listTask) {
        this.listTask = listTask;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtJenis.setText(listTask.get(position).getIdJenis());
        holder.txtDetailTask.setText(listTask.get(position).getDetailTask());
        holder.txtTanggal.setText(listTask.get(position).getTglDdline());

        if(listTask.get(position).getIdJenis().equals("1")){
            holder.txtJenis.setText("Quiz");
        } else if (listTask.get(position).getIdJenis().equals("2")){
            holder.txtJenis.setText("Assignment");
        }

        if(listTask.get(position).getStatus().equals("0")){
            holder.cbStatus.setChecked(false);
        } else if (listTask.get(position).getStatus().equals("1")){
            holder.cbStatus.setChecked(true);
        }

        holder.cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    setStatus(listTask.get(holder.getAdapterPosition()).getIdTask(), "1");
                } else {
                    setStatus(listTask.get(holder.getAdapterPosition()).getIdTask(), "0");
                }
            }
        });
    }

    private void setStatus(String idTask, String status) {
        api api = apiClient.getClient().create(api.class);
        Call<JsonObject> updateStatus = api.updateStatusTask(idTask, status);

        updateStatus.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful() && response.body() != null){
                    Log.i("onSuccess", response.body().toString());
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listTask.size();
    }

    public void setOnItemClicklistener(View.OnClickListener itemClicklistener){
        mOnItemClicklistener = itemClicklistener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtJenis, txtDetailTask, txtTanggal;
        CheckBox cbStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtJenis = itemView.findViewById(R.id.jenis_task);
            txtDetailTask = itemView.findViewById(R.id.detail_task);
            txtTanggal = itemView.findViewById(R.id.tanggal_task);
            cbStatus = itemView.findViewById(R.id.cb_task);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClicklistener);
        }
    }
}
