package com.co.coller.college;

import static com.co.coller.college.TaskFragment.DETAIL;
import static com.co.coller.college.TaskFragment.ID;
import static com.co.coller.college.TaskFragment.JENIS;
import static com.co.coller.college.TaskFragment.TANGGAL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.co.coller.R;
import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.co.coller.api.sharedPref;
import com.co.coller.college.schedule.AddScheduleActivity;
import com.google.gson.JsonObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText txtTanggal, txtDetail;
    Spinner spinner;
    Button btnBack, btnDelete,btnSimpan;
    sharedPref sharedPref;
    api api;
    Calendar mCalendar;
    String jenis_task;
    String id_task = null;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        txtTanggal = (EditText) findViewById(R.id.txt_tanggal_task);
        txtDetail = (EditText) findViewById(R.id.txt_detail_task);
        spinner = (Spinner) findViewById(R.id.spinner_jenis_task);
        btnBack = (Button) findViewById(R.id.button_x_task);
        btnDelete = (Button) findViewById(R.id.btn_hapus_task);
        btnSimpan = (Button) findViewById(R.id.btn_simpan_task);

        Intent intent = getIntent();
        sharedPref = new sharedPref(this);
        mCalendar = Calendar.getInstance();
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(AddTaskActivity.this, R.array.jenis_task, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spAdapter);

        id_task = intent.getStringExtra(ID);
        String jenis = intent.getStringExtra(JENIS);
        String tanggal = intent.getStringExtra(TANGGAL);
        String detail = intent.getStringExtra(DETAIL);

        if(id_task != null){
            txtTanggal.setText(tanggal);
            txtDetail.setText(detail);
            if(jenis.equals("1")){
                int spinnerPos = spAdapter.getPosition("Quiz");
                spinner.setSelection(spinnerPos);
            } else if (jenis.equals("2")){
                int spinnerPos = spAdapter.getPosition("Assignment");
                spinner.setSelection(spinnerPos);
            }
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showDialog();
                    deleteTask();
                }
            });
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jenis_task = adapterView.getItemAtPosition(i).toString();
                if(jenis_task.equals("Quiz")){
                    jenis_task = "1";
                } else {
                    jenis_task = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTanggal();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formCheck();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddTaskActivity.this, CollegeActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void showDialog() {
        dialog.setContentView(R.layout.alert_dialog_hapus);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        Button btnNo = dialog.findViewById(R.id.btn_no);

        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask();
                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void deleteTask() {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> deleteTask = api.deleteTask(id_task);

        deleteTask.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful() && response.body() != null){
                    Log.i("onSuccess", response.body().toString());
                    startActivity(new Intent(AddTaskActivity.this, CollegeActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getTanggal() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONDAY),
                mCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = year+"-"+(month+1)+"-"+dayOfMonth;
        txtTanggal.setText(date);
    }

    private void formCheck() {
        final String uid = sharedPref.getUid();
        final String jenis = jenis_task;
        final String tanggal = txtTanggal.getText().toString();
        final String detail = txtDetail.getText().toString();

        if (TextUtils.isEmpty(detail)) {
            txtDetail.setError("Please enter caption");
            txtDetail.requestFocus();
            return;
        }

        if (id_task == null){
            addTask(uid, detail, jenis, tanggal);
        } else {
            updateTask(id_task, detail, jenis, tanggal);
        }
    }

    private void updateTask(String id_task, String detail, String jenis, String tanggal) {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> updateTask = api.updateTask(id_task, detail, jenis, tanggal);

        updateTask.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful() && response.body() != null){
                    Log.i("onSuccess", response.body().toString());
                    startActivity(new Intent(AddTaskActivity.this, CollegeActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void addTask(String uid, String detail, String jenis, String tanggal) {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> addTask = api.addTask(uid, detail, jenis, tanggal);

        addTask.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful() && response.body() != null){
                    Log.i("onSuccess", response.body().toString());
                    startActivity(new Intent(AddTaskActivity.this, CollegeActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}