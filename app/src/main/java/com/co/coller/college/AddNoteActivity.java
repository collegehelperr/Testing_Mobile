package com.co.coller.college;

import static com.co.coller.college.NoteFragment.BODY;
import static com.co.coller.college.NoteFragment.ID;
import static com.co.coller.college.NoteFragment.JUDUL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteActivity extends AppCompatActivity {
    
    api api;
    sharedPref sharedPref;
    EditText edJudul, edBody;
    Button btnSimpan, btnBack;
    String id_note = null;
    
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

        edJudul.setText(judul);
        edBody.setText(body);

        
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