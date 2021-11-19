package com.co.coller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.co.coller.api.sharedPref;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfilActivity extends AppCompatActivity {

    ImageButton btnBack;
    CircleImageView fotoProf;
    Button btnSimpan;
    EditText txtNama, txtEmail, txtNohp;
    sharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        btnBack = (ImageButton) findViewById(R.id.btn_back_edit);
        fotoProf = (CircleImageView) findViewById(R.id.foto_profil_edit);
        btnSimpan = (Button) findViewById(R.id.btn_simpan);
        txtNama = (EditText) findViewById(R.id.txt_edit_nama);
        txtEmail = (EditText) findViewById(R.id.txt_edit_email);
        txtNohp = (EditText) findViewById(R.id.txt_edit_nohp);

        sharedPref = new sharedPref(this);
        txtNama.setText(sharedPref.getName());
        txtEmail.setText(sharedPref.getEmail());
        txtNohp.setText(sharedPref.getNohp());

        String urlImg = "https://b182-20-119-63-129.ngrok.io" + sharedPref.getProfImg();
        Log.i("Url image", urlImg);

        Glide.with(this).load(urlImg).into(fotoProf);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfilActivity.this, ProfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditProfilActivity.this, ProfilActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}