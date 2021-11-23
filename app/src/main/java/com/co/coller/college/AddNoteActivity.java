package com.co.coller.college;

import static com.co.coller.college.NoteFragment.BODY;
import static com.co.coller.college.NoteFragment.ID;
import static com.co.coller.college.NoteFragment.JUDUL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.co.coller.EditProfilActivity;
import com.co.coller.R;
import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.co.coller.api.sharedPref;
import com.co.coller.model.note;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteActivity extends AppCompatActivity {
    
    api api;
    sharedPref sharedPref;
    EditText edJudul, edBody;
    Button btnSimpan, btnBack, btnDelete;
    String id_note = null;
    Dialog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        
        sharedPref = new sharedPref(this);
        Intent intent = getIntent();

        String judul = intent.getStringExtra(JUDUL);
        String body = intent.getStringExtra(BODY);
        id_note = intent.getStringExtra(ID);
        
        edJudul = findViewById(R.id.txt_judul_note);
        edBody = findViewById(R.id.isi_note);
        btnSimpan = findViewById(R.id.btn_simpan_note);
        btnBack = findViewById(R.id.button_x_note);
        btnDelete = findViewById(R.id.btn_hapus_note);

        edJudul.setText(judul);
        edBody.setText(body);

        dialog = new Dialog(this);

        if(id_note != null){
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                    //hapusNote();
                }
            });
        }

        
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNoteActivity.this, CollegeActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formCheck();
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
                hapusNote();
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

    private void hapusNote() {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> deleteNote = api.deleteNote(id_note);

        deleteNote.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful() && response.body() != null){
                    Log.i("onSuccess", response.body().toString());
                    startActivity(new Intent(AddNoteActivity.this, CollegeActivity.class));
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

    private void formCheck() {
        final String uid = sharedPref.getUid();
        final String judul = edJudul.getText().toString();
        final String body = edBody.getText().toString();

        if (TextUtils.isEmpty(judul)) {
            edJudul.setError("Please enter caption");
            edJudul.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(body)) {
            edBody.setError("Please enter note");
            edBody.requestFocus();
            return;
        }

        if (id_note == null){
            saveNote(uid, judul, body);
        } else {
            updateNote(id_note, judul, body);
        }
    }

    private void updateNote(String id_note, String judul, String body) {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> updateNote = api.updateNote(id_note, judul, body);

        updateNote.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful() && response.body() != null){
                    Log.i("onSuccess", response.body().toString());
                    startActivity(new Intent(AddNoteActivity.this, CollegeActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(AddNoteActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNote(String uid, String judul, String body) {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> saveNote = api.addNote(uid, judul, body);

        saveNote.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful() && response.body() != null){
                    Log.i("onSuccess", response.body().toString());
                    startActivity(new Intent(AddNoteActivity.this, CollegeActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(AddNoteActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}