package com.co.coller.college.schedule;

import static com.co.coller.college.schedule.SeninFragment.HARI;
import static com.co.coller.college.schedule.SeninFragment.ID;
import static com.co.coller.college.schedule.SeninFragment.JAM_BERAKHIR;
import static com.co.coller.college.schedule.SeninFragment.JAM_MULAI;
import static com.co.coller.college.schedule.SeninFragment.NAMA;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.co.coller.DashboardActivity;
import com.co.coller.EditProfilActivity;
import com.co.coller.ProfilActivity;
import com.co.coller.R;
import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.co.coller.api.sharedPref;
import com.co.coller.college.AddNoteActivity;
import com.co.coller.college.CollegeActivity;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddScheduleActivity extends AppCompatActivity {

    EditText txtMulai, txtBerakhir, txtMatkul;
    int jam, menit;
    Spinner spinner;
    String dataSpinner;
    com.co.coller.api.api api;
    com.co.coller.api.sharedPref sharedPref;
    Button btnSimpan, btnDelete, btnBack;
    String id_schedule = null;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        Intent intent = getIntent();
        sharedPref = new sharedPref(this);
        dialog = new Dialog(this);

        String mulai = intent.getStringExtra(JAM_MULAI);
        String berakhir = intent.getStringExtra(JAM_BERAKHIR);
        String nama = intent.getStringExtra(NAMA);
        id_schedule = intent.getStringExtra(ID);
        String hari = intent.getStringExtra(HARI);

        txtMulai = (EditText) findViewById(R.id.txt_mulai);
        txtBerakhir = (EditText) findViewById(R.id.txt_berakhir);
        txtMatkul = (EditText) findViewById(R.id.txt_matkul);
        btnSimpan = (Button) findViewById(R.id.btn_simpan_schedule);
        btnDelete = (Button) findViewById(R.id.btn_hapus_schedule);
        spinner = findViewById(R.id.spinner_hari);
        btnBack = (Button) findViewById(R.id.button_x_schedule);

        if(id_schedule != null){
            txtMulai.setText(mulai);
            txtBerakhir.setText(berakhir);
            txtMatkul.setText(nama);
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //deleteSchedule();
                    showDialog();
                }
            });
        }

        txtMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddScheduleActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                jam = i;
                                menit = i1;
                                String time = jam + ":" + menit;
                                SimpleDateFormat f24jam = new SimpleDateFormat("HH:mm");

                                try {
                                    Date date = f24jam.parse(time);
                                    SimpleDateFormat jamMulai = new SimpleDateFormat("HH:mm");
                                    txtMulai.setText(jamMulai.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 00, true
                        );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(jam, menit);
                timePickerDialog.show();
            }
        });

        txtBerakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddScheduleActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                jam = i;
                                menit = i1;
                                String time = jam + ":" + menit;
                                SimpleDateFormat f24jam = new SimpleDateFormat("HH:mm");

                                try {
                                    Date date = f24jam.parse(time);
                                    SimpleDateFormat jamMulai = new SimpleDateFormat("HH:mm");
                                    txtBerakhir.setText(jamMulai.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 00, true
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(jam, menit);
                timePickerDialog.show();
            }
        });


        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(AddScheduleActivity.this, R.array.hari, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spAdapter);

        if(hari != null){
            int spinnerPos = spAdapter.getPosition(hari);
            spinner.setSelection(spinnerPos);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dataSpinner = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                startActivity(new Intent(AddScheduleActivity.this, CollegeActivity.class));
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
                deleteSchedule();
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

    private void deleteSchedule() {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> deleteSchedule = api.deleteSchedule(id_schedule);

        deleteSchedule.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful() && response.body() != null){
                    Log.i("onSuccess", response.body().toString());
                    startActivity(new Intent(AddScheduleActivity.this, CollegeActivity.class));
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

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddScheduleActivity.this, CollegeActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void formCheck() {
        final String uid = sharedPref.getUid();
        final String hari = dataSpinner;
        final String jamMulai = txtMulai.getText().toString();
        final String jamBerakhir = txtBerakhir.getText().toString();
        final String nama_matkul = txtMatkul.getText().toString();

        if (TextUtils.isEmpty(nama_matkul)) {
            txtMatkul.setError("Please enter caption");
            txtMatkul.requestFocus();
            return;
        }

        if (id_schedule == null){
            saveSchedule(uid, hari, jamMulai, jamBerakhir, nama_matkul);
        } else {
            updateSchedule(id_schedule,hari, jamMulai, jamBerakhir, nama_matkul);
        }
    }

    private void updateSchedule(String id_schedule, String hari, String jamMulai, String jamBerakhir, String nama_matkul) {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> updateSchedule = api.updateSchedule(id_schedule, hari, jamMulai, jamBerakhir, nama_matkul);

        updateSchedule.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful() && response.body() != null){
                    Log.i("onSuccess", response.body().toString());
                    startActivity(new Intent(AddScheduleActivity.this, CollegeActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(AddScheduleActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSchedule(String uid, String hari, String jamMulai, String jamBerakhir, String nama_matkul) {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> addSchedule = api.addSchedule(uid, hari, jamMulai, jamBerakhir, nama_matkul);

        addSchedule.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful() && response.body() != null){
                    Log.i("onSuccess", response.body().toString());
                    startActivity(new Intent(AddScheduleActivity.this, CollegeActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(AddScheduleActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}